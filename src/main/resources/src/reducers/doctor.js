import {
  DOCTOR_LOADED,
  DOCTOR_PATIENT_DIAGNOSED,
  DOCTOR_PATIENT_TREATED,
  DOCTOR_SICK_PATIENT_LIST_LOADED,
  DOCTOR_TREAT_PATIENT_LIST_LOADED,
} from "../actions/types";

const initialState = {
  doctor: {},
  doctor_treat_patients: [],
  doctor_sick_patients: [],
};

export default function (state = initialState, action) {
  switch (action.type) {
    case DOCTOR_LOADED:
      return {
        ...state,
        doctor: action.payload,
      };
    case DOCTOR_TREAT_PATIENT_LIST_LOADED:
      return {
        ...state,
        doctor_treat_patients: action.payload,
      };
    case DOCTOR_PATIENT_TREATED:
      return {
        ...state,
        doctor_treat_patients: state.doctor_treat_patients.filter(
          (a_patient) => a_patient.id !== action.payload.id
        ),
      };
    case DOCTOR_SICK_PATIENT_LIST_LOADED:
      return {
        ...state,
        doctor_sick_patients: action.payload,
      };
    case DOCTOR_PATIENT_DIAGNOSED:
      console.log(action.payload);
      return {
        ...state,
        doctor_sick_patients: state.doctor_sick_patients.filter(
          (a_patient) => a_patient.id !== action.payload.id
        ),
        doctor_treat_patients: state.doctor_treat_patients.concat([
          action.payload,
        ]),
      };
    default:
      return state;
  }
}
