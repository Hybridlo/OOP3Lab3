import axios from "axios";
import { returnErrors } from "./messages";
import { tokenConfig } from "./auth";

import { PATIENT_LOADED, PATIENT_SET_SICK } from "./types";

// GET PATIENT
export const getPatient = (id) => (dispatch, getState) => {
  axios
    .get(`/patient/${id}`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: PATIENT_LOADED,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};

// SET SICK PATIENT
export const setSickPatient = (id) => (dispatch, getState) => {
  axios
    .post(`/patient/${id}/setsick`, tokenConfig(getState))
    .then((res) => {
      dispatch({
        type: PATIENT_SET_SICK,
        payload: res.data,
      });
    })
    .catch((err) => {
      dispatch(returnErrors(err.response.data, err.response.status));
    });
};
