import axios from "axios";
import { returnErrors } from "./messages";
import { tokenConfig } from "./auth";

import {
  NURSE_LOADED,
  NURSE_PATIENT_LIST_LOADED,
  NURSE_PATIENT_TREATED,
} from "./types";

// GET NURSE
export const getNurse = (id) => (dispatch, getState) => {
  axios
    .get(`/nurse/${id}`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: NURSE_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const nurseGetTreatablePatients = () => (dispatch, getState) => {
  axios
    .get(`/nurse/treatable_patients`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: NURSE_PATIENT_LIST_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

export const nurseTreatPatient = (id) => (dispatch, getState) => {
  axios
    .post(`/nurse/treat_patient/${id}`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: NURSE_PATIENT_TREATED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};
