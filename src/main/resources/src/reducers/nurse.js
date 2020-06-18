import {
  NURSE_LOADED,
  NURSE_PATIENT_LIST_LOADED,
  NURSE_PATIENT_TREATED,
} from "../actions/types";

const initialState = {
  nurse: {},
  nurse_patients: [],
};

export default function (state = initialState, action) {
  switch (action.type) {
    case NURSE_LOADED:
      return {
        ...state,
        nurse: action.payload,
      };
    case NURSE_PATIENT_LIST_LOADED:
      return {
        ...state,
        nurse_patients: action.payload,
      };
    case NURSE_PATIENT_TREATED:
      return {
        ...state,
        nurse_patients: state.nurse_patients.filter(
          (a_patient) => a_patient.id !== action.payload.id
        ),
      };
    default:
      return state;
  }
}
