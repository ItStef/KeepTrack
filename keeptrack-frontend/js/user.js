// Check authentication on page load
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    
    if (!token || role !== 'USER') {
        document.getElementById('user-content').style.display = 'none';
        document.getElementById('unauth-msg').style.display = 'block';
        return;
    }
    
    document.getElementById('user-content').style.display = 'block';
    document.getElementById('unauth-msg').style.display = 'none';
    document.getElementById('logout-button').addEventListener('click', logout);
    
    // Load initial data
    displayProjectsWithTasks();
    displayTasksWithTags();
    loadTasks();
    loadTags();
});

// Tag Creation
document.getElementById('create-tag-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const name = document.getElementById('tag-name').value;
    
    try {
        const response = await fetch('http://localhost:8080/api/tags', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({ name })
        });
        
        if (response.ok) {
            alert('Tag created successfully!');
            loadTags();
            displayTasksWithTags();
            document.getElementById('tag-name').value = '';
        } else {
            throw new Error('Failed to create tag');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to create tag');
    }
});

// Add Tags to Task
document.getElementById('add-tags-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const taskId = document.getElementById('task-select').value;
    const selectedTags = Array.from(document.getElementById('task-tags').selectedOptions)
        .map(option => parseInt(option.value));
    
    try {
        const response = await fetch(`http://localhost:8080/api/tasks/${taskId}/tags`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(selectedTags)
        });
        
        if (response.ok) {
            alert('Tags updated successfully!');
            displayTasksWithTags();
            document.getElementById('task-select').value = '';
            document.getElementById('task-tags').selectedIndex = -1;
        } else {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Failed to update tags');
        }
    } catch (error) {
        console.error('Error:', error);
        alert(error.message || 'Failed to update tags');
    }
});

// Load Tasks
async function loadTasks() {
    try {
        const response = await fetch('http://localhost:8080/api/tasks', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const tasks = await response.json();
        
        const taskSelect = document.getElementById('task-select');
        taskSelect.innerHTML = '<option value="">Select Task</option>';
        tasks.forEach(task => {
            const option = document.createElement('option');
            option.value = task.id;
            option.textContent = task.name;
            taskSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading tasks:', error);
    }
}

// Load Tags
async function loadTags() {
    try {
        const response = await fetch('http://localhost:8080/api/tags', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const tags = await response.json();
        
        const taskTags = document.getElementById('task-tags');
        taskTags.innerHTML = ''; // Clear existing options
        tags.forEach(tag => {
            const option = document.createElement('option');
            option.value = tag.id;
            option.textContent = tag.name;
            taskTags.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading tags:', error);
    }
}

async function displayProjectsWithTasks() {
    try {
        const response = await fetch('http://localhost:8080/api/projects', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const projects = await response.json();
        
        const projectsTasksList = document.getElementById('projects-tasks-list');
        projectsTasksList.innerHTML = '';
        
        projects.forEach(project => {
            const projectTasks = project.tasks || [];
            const tasksHtml = projectTasks.map(task => `
                <div class="sub-item">
                    <strong>${task.name}</strong><br>
                    ${task.description || 'No description'}
                </div>
            `).join('');
            
            projectsTasksList.innerHTML += `
                <div class="main-item">
                    <div class="item-header">
                        <h4>${project.title}</h4>
                    </div>
                    <div class="sub-items">
                        ${tasksHtml || '<div class="sub-item">No tasks yet</div>'}
                    </div>
                </div>`;
        });
    } catch (error) {
        console.error('Error displaying projects with tasks:', error);
    }
}

async function displayTasksWithTags() {
    try {
        const response = await fetch('http://localhost:8080/api/tasks', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const tasks = await response.json();
        
        const tasksTagsList = document.getElementById('tasks-tags-list');
        tasksTagsList.innerHTML = '';
        
        tasks.forEach(task => {
            const taskTags = task.tags || [];
            const tagsHtml = taskTags.map(tag => `
                <span class="tag">${tag.name}</span>
            `).join('');
            
            tasksTagsList.innerHTML += `
                <div class="main-item">
                    <div class="item-header">
                        <h4>${task.name}</h4>
                        <div class="description">${task.description || 'No description'}</div>
                    </div>
                    <div class="sub-items">
                        ${tagsHtml || '<div class="no-tags">No tags assigned</div>'}
                    </div>
                </div>`;
        });
    } catch (error) {
        console.error('Error displaying tasks with tags:', error);
    }
}

// Logout function
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = 'login.html';
}