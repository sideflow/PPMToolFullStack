import { GET_PROJECTS, GET_PROJECT } from '../actions/types';

const initialStates = {
	projects: [],
	project: {}
};

export default function(state = initialStates, action) {
	switch (action.type) {
		case GET_PROJECTS:
			return {
				...state,
				projects: action.payload
			};
		case GET_PROJECT:
			return {
				...state,
				project: action.payload
			};
		default:
			return state;
	}
}
