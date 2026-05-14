const API_URL = 'http://localhost:8080/api/tasks';

// Load tasks when page loads
document.addEventListener('DOMContentLoaded', () => {
    loadTasks();
    
    // Set default due date to tomorrow at 9 AM
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(9, 0, 0);
    const formattedDate = tomorrow.toISOString().slice(0, 16);
    document.getElementById('dueDateTime').value = formattedDate;
});

// Handle form submission
document.getElementById('taskForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // Get form values
    const taskData = {
        title: document.getElementById('title').value.trim(),
        description: document.getElementById('description').value.trim() || null,
        status: document.getElementById('status').value,
        dueDateTime: document.getElementById('dueDateTime').value
    };

    // Validate
    if (!taskData.title) {
        showMessage('Please enter a task title', 'error', 'formMessage');
        return;
    }

    if (!taskData.dueDateTime) {
        showMessage('Please select a due date/time', 'error', 'formMessage');
        return;
    }

    try {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(taskData)
        });

        if (response.ok) {
            const savedTask = await response.json();
            showMessage(` Task "${savedTask.title}" created successfully!`, 'success', 'formMessage');
            document.getElementById('taskForm').reset();
            
            // Reset due date to tomorrow
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            tomorrow.setHours(9, 0, 0);
            document.getElementById('dueDateTime').value = tomorrow.toISOString().slice(0, 16);
            
            // Reload tasks
            loadTasks();
        } else {
            const error = await response.text();
            showMessage(` Failed to create task: ${error}`, 'error', 'formMessage');
        }
    } catch (error) {
        showMessage(` Error: ${error.message}. Make sure the backend is running on ${API_URL}`, 'error', 'formMessage');
    }
});

// Load all tasks
async function loadTasks() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) {
            throw new Error('Failed to load tasks');
        }
        
        const tasks = await response.json();
        displayTasks(tasks);
    } catch (error) {
        document.getElementById('tasksList').innerHTML = `
            <div class="empty-state">
                 Error loading tasks: ${error.message}<br>
                Make sure the backend is running on ${API_URL}
            </div>
        `;
    }
}

// Display tasks
function displayTasks(tasks) {
    const tasksListDiv = document.getElementById('tasksList');
    
    if (!tasks || tasks.length === 0) {
        tasksListDiv.innerHTML = '<div class="empty-state"> No tasks yet. Create your first task above!</div>';
        return;
    }

    tasksListDiv.innerHTML = tasks.map(task => `
        <div class="task-card">
            <div class="task-title">${escapeHtml(task.title)}</div>
            ${task.description ? `<div class="task-description">${escapeHtml(task.description)}</div>` : ''}
            <div class="task-details">
                <span> ID: ${task.id}</span>
                <span> Due: ${formatDate(task.dueDateTime)}</span>
                <span> Created: ${formatDate(task.createdAt)}</span>
            </div>
            <div>
                <span class="task-status status-${task.status}">${task.status}</span>
            </div>
            <div class="task-actions">
                ${task.status !== 'COMPLETED' ? `<button class="btn-complete" onclick="updateStatus(${task.id})">✓ Mark Complete</button>` : ''}
                <button class="btn-delete" onclick="deleteTask(${task.id})">🗑 Delete</button>
            </div>
        </div>
    `).join('');
}

// Update task status to COMPLETED
async function updateStatus(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({status: 'COMPLETED'})
        });

        if (response.ok) {
            showMessage('Task marked as completed!', 'success', 'formMessage');
            loadTasks();
        } else {
            showMessage('Failed to update status', 'error', 'formMessage');
        }
    } catch (error) {
        showMessage(`Error: ${error.message}`, 'error', 'formMessage');
    }
}

async function updateTask(id, title, description, dueDate, status) {
    await fetch(`${API_URL}${id}`, {
        method: 'PATCH',
        body: JSON.stringify({
            title, description, dueDateTime: dueDate, status
        })
    });
}

// Delete task
async function deleteTask(id) {
    if (!confirm('Are you sure you want to delete this task?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showMessage('Task deleted successfully!', 'success', 'formMessage');
            loadTasks();
        } else {
            showMessage('Failed to delete task', 'error', 'formMessage');
        }
    } catch (error) {
        showMessage(`Error: ${error.message}`, 'error', 'formMessage');
    }
}

// Helper: Format date
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Helper: Escape HTML to prevent XSS
function escapeHtml(str) {
    if (!str) return '';
    return str
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// Helper: Show message
function showMessage(message, type, elementId) {
    const messageDiv = document.getElementById(elementId);
    messageDiv.textContent = message;
    messageDiv.className = `message ${type}`;
    
    setTimeout(() => {
        messageDiv.className = 'message';
        messageDiv.textContent = '';
    }, 3000);
}