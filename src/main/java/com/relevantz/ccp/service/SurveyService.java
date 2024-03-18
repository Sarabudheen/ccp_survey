package com.relevantz.ccp.service;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.relevantz.ccp.model.Department;
import com.relevantz.ccp.model.Project;
import com.relevantz.ccp.model.Region;
import com.relevantz.ccp.model.Survey;
import com.relevantz.ccp.model.SurveyDTO;
import com.relevantz.ccp.repository.DepartmentRepo;
import com.relevantz.ccp.repository.ProjectRepo;
import com.relevantz.ccp.repository.RegionRepo;
import com.relevantz.ccp.repository.SurveyRepo;

import jakarta.ws.rs.NotFoundException;

@Service
public class SurveyService {

	@Autowired
	SurveyRepo srRepo;

	@Autowired
	RegionRepo rgRepo;

	@Autowired
	DepartmentRepo dtRepo;

	@Autowired
	ProjectRepo ptRepo;

	@Autowired
	Survey survey;

	@Autowired
	Region region;

	@Autowired
	Department department;

	@Autowired
	Project project;

	public boolean insert(SurveyDTO surveydto) {

		survey.setSurveyId(srRepo.count() + 1);
		survey.setSurveyName(surveydto.getSurveyName());
		survey.setEndDate(surveydto.getEndDate());


		Optional<Region> optionalRegion = rgRepo.findById(surveydto.getRegion());
		if (optionalRegion.isPresent()) {
			region = optionalRegion.get();
			survey.setRegion(region);
		} else {
			throw new NotFoundException();
		}


		Optional<Department> optionalDepartment = dtRepo.findById(surveydto.getDepartment());
		if (optionalDepartment.isPresent()) {
			department = optionalDepartment.get();
			survey.setDepartment(department);
		} else {
			throw new NotFoundException();
		}

		
		Optional<Project> optionalProject = ptRepo.findById(surveydto.getProject());
		if (optionalProject.isPresent()) {
			project = optionalProject.get();
			survey.setProject(project);
		} else {
			throw new NotFoundException();
		}

		survey.setStatus("open");
		srRepo.save(survey);
		return true;
	}

	public boolean update(Survey survey) {
		srRepo.save(survey);
		return true;
	}

	public boolean delete(long surveyId) {
		srRepo.deleteById(surveyId);
		return true;
	}

	public List<Survey> getSurveyDetails(String surveyName) {
		Iterator<Survey> it1 = srRepo.findBySurveyName(surveyName).iterator();
		ArrayList<Survey> surveyList = new ArrayList<>();
		while (it1.hasNext()) {
			surveyList.add(it1.next());
		}
		return surveyList;
	}

	public List<Survey> getAllSurveyDetails() {
		Iterator<Survey> it = srRepo.findAll().iterator();
		ArrayList<Survey> list = new ArrayList<>();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}

}
