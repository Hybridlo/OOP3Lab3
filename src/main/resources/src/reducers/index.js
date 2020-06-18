import { combineReducers } from "redux";
import data from "./data";
import errors from "./errors";
import messages from "./messages";
import auth from "./auth";
import patient from "./patient";
import nurse from "./nurse";
import doctor from "./doctor";

export default combineReducers({
  data,
  errors,
  messages,
  auth,
  patient,
  nurse,
  doctor,
});
