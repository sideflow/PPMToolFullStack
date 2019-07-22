import { GET_PROJECTS } from '../actions/types';

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
		default:
			return state;
	}
}
