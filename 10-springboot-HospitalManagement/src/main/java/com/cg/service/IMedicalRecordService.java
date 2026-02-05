package com.cg.service;

import java.util.List;

import com.cg.model.MedicalRecord;

public interface IMedicalRecordService {
	List<MedicalRecord>getMedicalRecordByPatientUsername(String username);

}
