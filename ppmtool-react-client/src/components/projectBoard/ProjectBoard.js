import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import Backlog from './Backlog';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { getBacklog } from '../../actions/BacklogActions';
import { Stats } from 'fs';

class ProjectBoard extends Component {
	constructor() {
		super();
		this.state = {
			errors: {}
		};
	}
	componentDidMount() {
		const { id } = this.props.match.params;
		const { project_tasks } = this.props.backlog;
		const { errors } = this.state;
		this.props.getBacklog(id);
	}

	componentWillReceiveProps(nextProps) {
		if (nextProps.errors) {
			this.setState({ errors: nextProps.errors });
		}
	}

	render() {
		const { id } = this.props.match.params;
		const { project_tasks } = this.props.backlog;
		const { errors } = this.state;

		let boardContent;

		const boardAlgorithm = (errors, project_tasks) => {
			if (project_tasks.length < 1) {
				if (errors.projectIdentifier) {
					return <div className="alert alert-danger text-center">{errors.projectIdentifier}</div>;
				} else {
					return (
						<div className="alert-info text-center" role="alert">
							No tasks in this board
						</div>
					);
				}
			} else {
				return <Backlog project_tasks={project_tasks} />;
			}
		};

		boardContent = boardAlgorithm(errors, project_tasks);

		return (
			<div className="container">
				<Link to={`/addProjectTask/${id}`} className="btn btn-primary mb-3">
					<i className="fas fa-plus-circle"> Create Project Task</i>
				</Link>
				<br />
				<hr />
				{boardContent}
			</div>
		);
	}
}

ProjectBoard.propTypes = {
	getBacklog: PropTypes.func.isRequired,
	backlog: PropTypes.object.isRequired,
	errors: PropTypes.object.isRequired
};

const mapStateToProps = (state) => ({
	backlog: state.backlog,
	errors: state.errors
});

export default connect(mapStateToProps, { getBacklog })(ProjectBoard);
