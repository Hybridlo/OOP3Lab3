import { PATIENT_LOADED, PATIENT_SET_SICK } from "../actions/types";

const initialState = {
  patient: {},
};

export default function (state = initialState, action) {
  switch (action.type) {
    case PATIENT_LOADED:
    case PATIENT_SET_SICK:
      return {
        patient: action.payload,
      };
    default:
      return state;
  }
}
