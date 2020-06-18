import axios from "axios";
import { createMessage, returnErrors } from "./messages";

import { GET_DATA, GET_ERRORS } from "./types";
import { tokenConfig } from "./auth";

// GET_DATA
export const getData = () => (dispatch, getState) => {
  axios
    .get("/patient/1", tokenConfig(getState))
    .then((res) => {
      dispatch(createMessage({ pollData: "Data polled" }));
      dispatch({
        type: GET_DATA,
        payload: res.data,
      });
    })
    .catch((err) =>
      dispatch(returnErrors(err.response.data, err.response.status))
    );
};
