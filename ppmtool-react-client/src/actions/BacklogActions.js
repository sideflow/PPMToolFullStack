import axios from 'axios';
import { GET_ERRORS, GET_BACKLOG } from './types';
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
