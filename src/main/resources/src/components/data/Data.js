import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getPatient, setSickPatient } from "../../actions/patient";
import {
  getNurse,
  nurseGetTreatablePatients,
  nurseTreatPatient,
} from "../../actions/nurse";
import {
  getDoctor,
  doctorGetTreatablePatients,
  doctorTreatPatient,
  doctorGetSickPatients,
  doctorDiagnosePatient,
} from "../../actions/doctor";

export class Data extends Component {
  state = {
    diagnose: "",
  };

  onChange = (e) => this.setState({ [e.target.name]: e.target.value });

  doctorDiagnose = (e) => {
    e.preventDefault();
    this.props.doctorDiagnosePatient(e.target.id, this.state.diagnose);
  };

  static propTypes = {
    patient: PropTypes.object.isRequired,
    nurse: PropTypes.object.isRequired,
    nurse_patients: PropTypes.array.isRequired,
    doctor: PropTypes.object.isRequired,
    doctor_sick_patients: PropTypes.array.isRequired,
    doctor_treat_patients: PropTypes.array.isRequired,
    auth: PropTypes.object.isRequired,
  };

  componentDidMount() {
    switch (this.props.auth.user.type) {
      case "patient":
        this.props.getPatient(this.props.auth.user.id);
        break;
      case "nurse":
        this.props.getNurse(this.props.auth.user.id);
        this.props.nurseGetTreatablePatients();
        break;
      case "doctor":
        this.props.getDoctor(this.props.auth.user.id);
        this.props.doctorGetTreatablePatients();
        this.props.doctorGetSickPatients();
        break;
    }
  }

  render() {
    const { diagnose } = this.state;
    const { type, id } = this.props.auth.user;
    const {
      patient,
      setSickPatient,
      nurse,
      nurse_patients,
      nurseTreatPatient,
      doctor,
      doctor_treat_patients,
      doctor_sick_patients,
      doctorTreatPatient,
    } = this.props;

    const patientData = (
      <span>
        <p>
          name: "{patient.name}"; is sick: "{String(patient.isSick)}"; sickness:
          "{patient.sickness}"; treatment: "{patient.treatment}"; sickness
          history: "{patient.sicknessHistory}";
        </p>
        {patient.isSick ? (
          ""
        ) : (
          <button onClick={setSickPatient.bind(this, id)}>Set sick</button>
        )}
      </span>
    );

    const nurseData = (
      <span>
        <p>Nurse: "{nurse.login}"</p>
        <h3>Patients</h3>
        {nurse_patients.map((a_patient) => (
          <p>
            name: "{a_patient.name}"; is sick: "{String(a_patient.isSick)}";
            sickness: "{a_patient.sickness}"; treatment: "{a_patient.treatment}
            "; sickness history: "{a_patient.sicknessHistory}";
            <button
              id={a_patient.id}
              onClick={nurseTreatPatient.bind(this, a_patient.id)}
            >
              treat
            </button>
          </p>
        ))}
      </span>
    );

    const doctorData = (
      <span>
        <p>Doctor: "{doctor.login}"</p>
        <h3>Sick patients</h3>
        <h4>Diagnose:</h4>
        <input
          type="text"
          className="form-control"
          name="diagnose"
          onChange={this.onChange}
          value={diagnose}
        />
        {doctor_sick_patients.map((a_patient) => (
          <p>
            name: "{a_patient.name}"; is sick: "{String(a_patient.isSick)}";
            sickness: "{a_patient.sickness}"; treatment: "{a_patient.treatment}
            "; sickness history: "{a_patient.sicknessHistory}";
            <button id={a_patient.id} onClick={this.doctorDiagnose}>
              diagnose
            </button>
          </p>
        ))}
        <h3>Treatable patients</h3>
        {doctor_treat_patients.map((a_patient) => (
          <p>
            name: "{a_patient.name}"; is sick: "{String(a_patient.isSick)}";
            sickness: "{a_patient.sickness}"; treatment: "{a_patient.treatment}
            "; sickness history: "{a_patient.sicknessHistory}";
            <button
              id={a_patient.id}
              onClick={doctorTreatPatient.bind(this, a_patient.id)}
            >
              treat
            </button>
          </p>
        ))}
      </span>
    );

    return (
      <Fragment>
        {type === "patient" ? patientData : ""}
        {type === "nurse" ? nurseData : ""}
        {type === "doctor" ? doctorData : ""}
      </Fragment>
    );
  }
}

const mapStateToProps = (state) => ({
  patient: state.patient.patient,
  nurse: state.nurse.nurse,
  nurse_patients: state.nurse.nurse_patients,
  doctor: state.doctor.doctor,
  doctor_treat_patients: state.doctor.doctor_treat_patients,
  doctor_sick_patients: state.doctor.doctor_sick_patients,
  auth: state.auth,
});

export default connect(mapStateToProps, {
  getPatient,
  setSickPatient,
  getNurse,
  nurseGetTreatablePatients,
  nurseTreatPatient,
  getDoctor,
  doctorGetTreatablePatients,
  doctorTreatPatient,
  doctorGetSickPatients,
  doctorDiagnosePatient,
})(Data);
