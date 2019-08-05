import React, { Component } from 'react';
import ProjectTask from './projectTasks/ProjectTask';

class Backlog extends Component {
	render() {
		const { project_tasks } = this.props;

		const tasks = project_tasks.map((project_task) => (
			<ProjectTask key={project_task.id} project_task={project_task} />
		));

		let toDoItems = [];
		let inProgressItems = [];
		let doneItems = [];

		for (let i = 0; i < tasks.length; i++) {
			if (tasks[i].props.project_task.status === 'TO_DO') {
				toDoItems.push(tasks[i]);
			} else if (tasks[i].props.project_task.status === 'IN_PROGRESS') {
				inProgressItems.push(tasks[i]);
			} else if (tasks[i].props.project_task.status === 'DONE') {
				doneItems.push(tasks[i]);
			}
		}

		return (
			<div class="container">
				<div class="row">
					<div class="col-md-4">
						<div class="card text-center mb-2">
							<div class="card-header bg-secondary text-white">
								<h3>TO DO</h3>
							</div>
						</div>
						{toDoItems}
					</div>
					<div class="col-md-4">
						<div class="card text-center mb-2">
							<div class="card-header bg-primary text-white">
								<h3>In Progress</h3>
							</div>
						</div>
						{inProgressItems}
					</div>
					<div class="col-md-4">
						<div class="card text-center mb-2">
							<div class="card-header bg-success text-white">
								<h3>Done</h3>
							</div>
						</div>
						{doneItems}
					</div>
				</div>
			</div>
		);
	}
}

export default Backlog;
