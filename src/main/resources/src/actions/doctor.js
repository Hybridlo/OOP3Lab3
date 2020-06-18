import axios from "axios";
import { returnErrors } from "./messages";
import { tokenConfig } from "./auth";

import {
  DOCTOR_LOADED,
  DOCTOR_PATIENT_DIAGNOSED,
  DOCTOR_PATIENT_TREATED,
  DOCTOR_SICK_PATIENT_LIST_LOADED,
  DOCTOR_TREAT_PATIENT_LIST_LOADED,
} from "./types";

// GET NURSE
export const getDoctor = (id) => (dispatch, getState) => {
  axios
    .get(`/doctor/${id}`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: DOCTOR_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const doctorGetTreatablePatients = () => (dispatch, getState) => {
  axios
    .get(`/doctor/treatable_patients`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: DOCTOR_TREAT_PATIENT_LIST_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const doctorTreatPatient = (id) => (dispatch, getState) => {
  axios
    .post(`/doctor/treat_patient/${id}`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: DOCTOR_PATIENT_TREATED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const doctorGetSickPatients = () => (dispatch, getState) => {
  axios
    .get(`/doctor/sick_patients`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: DOCTOR_SICK_PATIENT_LIST_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const doctorDiagnosePatient = (id, diagnose) => (dispatch, getState) => {
  const body = JSON.stringify({ diagnose });

  axios
    .post(`/doctor/diagnose_patient/${id}`, body, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: DOCTOR_PATIENT_DIAGNOSED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};
