document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    
    if (!token || role !== 'BOSS') {
        document.getElementById('boss-content').style.display = 'none';
        document.getElementById('unauth-msg').style.display = 'block';
        return;
    }
    
    document.getElementById('boss-content').style.display = 'block';
    document.getElementById('unauth-msg').style.display = 'none';
    document.getElementById('logout-button').addEventListener('click', logout);

    displayProjectsWithTasks();
    displayTasksWithTags();
    loadProjects();
    loadTasks();
    loadTags();
});

// Project Creation
document.getElementById('create-project-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const title = document.getElementById('project-title').value;
    
    try {
        const response = await fetch('http://localhost:8080/api/projects', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({ title })
        });
        
        if (response.ok) {
            alert('Project created successfully!');
            loadProjects();
            displayProjectsWithTasks();
            document.getElementById('project-title').value = '';
        } else {
            throw new Error('Failed to create project');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to create project');
    }
});

// Task Creation
document.getElementById('create-task-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const name = document.getElementById('task-name').value;
    const description = document.getElementById('task-description').value;
    const projectId = document.getElementById('project-select').value;
    
     const selectedTags = Array.from(document.getElementById('task-tags').selectedOptions)
        .map(option => parseInt(option.value));
    try {
        const response = await fetch('http://localhost:8080/api/tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({
                name,
                description,
                projectId,
                tagIds: selectedTags
            })
        });
        
        if (response.ok) {
            alert('Task created successfully!');
            loadTasks();
            displayProjectsWithTasks();
            displayTasksWithTags();
            document.getElementById('task-name').value = '';
            document.getElementById('task-description').value = '';
            document.getElementById('project-select').value = '';
            document.getElementById('task-tags').selectedIndex = -1; // Reset tags selection
        } else {
            throw new Error('Failed to create task');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to create task');
    }
});

// Tag Creation
document.getElementById('create-tag-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const name = document.getElementById('tag-name').value;
    
    // Make project
    try {
        const response = await fetch('http://localhost:8080/api/tags', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({ name })
        });
        
        // Check if the request was successful
        if (response.ok) {
            alert('Tag created successfully!');
            loadTags();
            document.getElementById('tag-name').value = '';
        } else {
            throw new Error('Failed to create tag');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Failed to create tag');
    }
});

// Load Projects
async function loadProjects() {
    try {
        const response = await fetch('http://localhost:8080/api/projects', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const projects = await response.json();
        
        // Update project select
        const projectSelect = document.getElementById('project-select');
        projectSelect.innerHTML = '';
        projects.forEach(project => {
            const option = document.createElement('option');
            option.value = project.id;
            option.textContent = project.title;
            projectSelect.appendChild(option);
        });
        
        // Display projects list
        const projectsList = document.getElementById('projects-list');
        projectsList.innerHTML = '<h4>Projects:</h4>';
        projects.forEach(project => {
            projectsList.innerHTML += `<div class="item">${project.title}</div>`;
        });
    } catch (error) {
        console.error('Error loading projects:', error);
    }
}

// Load Tasks
async function loadTasks() {
    try {
        const response = await fetch('http://localhost:8080/api/tasks', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        const tasks = await response.json();
        
        const tasksList = document.getElementById('tasks-list');
        tasksList.innerHTML = '<h4>Tasks:</h4>';
        tasks.forEach(task => {
            tasksList.innerHTML += `
                <div class="item">
                    <strong>${task.name}</strong><br>
                    ${task.description}
                </div>`;
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
        
    // Update task tags select
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

// Display Projects with Tasks

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

// Display Tasks with Tags

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
    window.location.href = 'index.html';
}