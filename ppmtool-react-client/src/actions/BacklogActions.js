import axios from 'axios';
import { GET_ERRORS, GET_BACKLOG, GET_PROJECT_TASK } from './types';
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

export const updateProjectTask = (backlogId, projectTask, history) => async (dispatch) => {
	try {
		await axios.patch(`http://localhost:8081/api/backlog/${backlogId}`, projectTask);
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
		history.push('/dashboard');
		dispatch({
			type: GET_ERRORS,
			payload: err.response.data
		});
	}
};
