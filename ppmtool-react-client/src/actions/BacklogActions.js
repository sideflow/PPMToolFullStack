import axios from 'axios';
import { GET_ERRORS, GET_BACKLOG, GET_PROJECT_TASK, DELETE_PROJECT_TASK } from './types';
import { async } from 'q';

export const addProjectTask = (backlogId, projectTask, history) => async (dispatch) => {
	try {
		await axios.post(`http://localhost:8081/api/backlog/${backlogId}`, projectTask);
		history.push(`/projectBoard/${backlogId}`);
		dispatch({
			type: GET_ERRORS,
			payload: {}
		});
	} catch (err) {
		dispatch({
			type: GET_ERRORS,
			payload: err.response.data
		});
	}
};

export const updateProjectTask = (backlogId, projectTaskId, projectTask, history) => async (dispatch) => {
	try {
		await axios.patch(`http://localhost:8081/api/backlog/${backlogId}/${projectTaskId}`, projectTask);
		history.push(`/projectBoard/${backlogId}`);
		dispatch({
			type: GET_ERRORS,
			payload: {}
		});
	} catch (err) {
		history.push('/dashboard');
		dispatch({
			type: GET_ERRORS,
			payload: err.response.data
		});
	}
};

export const deleteProjectTask = (backlogId, projectTaskId) => async (dispatch) => {
	//if (window.confirm(`You are deleting project task ${projectTaskId}, this action cannot be undone.`)) {
	await axios.delete(`http://localhost:8081/api/backlog/${backlogId}/${projectTaskId}`);
	dispatch({
		type: DELETE_PROJECT_TASK,
		payload: projectTaskId
	});
	//}
};

export const getBacklog = (backlogId) => async (dispatch) => {
	try {
		const res = await axios.get(`http://localhost:8081/api/backlog/${backlogId}`);
		dispatch({
			type: GET_BACKLOG,
			payload: res.data
		});
	} catch (err) {
		dispatch({
			type: GET_ERRORS,
			payload: err.response.data
		});
	}
};

export const getProjectTask = (backlog_id, projet_task_id, history) => async (dispatch) => {
	try {
		const res = await axios.get(`http://localhost:8081/api/backlog/${backlog_id}/${projet_task_id}`);
		dispatch({
			type: GET_PROJECT_TASK,
			payload: res.data
		});
	} catch (err) {
		dispatch({
			type: GET_ERRORS,
			payload: err.response.data
		});
	}
};
