package com.relevantz.ccp.service;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.relevantz.ccp.model.Options;
import com.relevantz.ccp.model.Questions;

import com.relevantz.ccp.model.ResponseDetails;
import com.relevantz.ccp.model.Responses;
import com.relevantz.ccp.model.ResponsesDTO;
import com.relevantz.ccp.repository.OptionRepo;
import com.relevantz.ccp.repository.QuestionsRepo;
import com.relevantz.ccp.repository.ResponseDetailsRepo;
import com.relevantz.ccp.repository.ResponseRepo;

import jakarta.ws.rs.NotFoundException;

@Service
public class ResponseService {

	@Autowired
	ResponseRepo rsRepo;

	@Autowired
	OptionRepo opRepo;

	@Autowired
	QuestionsRepo qnRepo;

	@Autowired
	Responses responses;

	@Autowired
	ResponseDetailsRepo resDetailRepo;

	public boolean insert(ResponsesDTO responsesdto) {

		long opt = opRepo.findByOptionId(responsesdto.getOptionId());

		responses.setResponseId(rsRepo.count() + 1);

		Optional<Options> optionalOption = opRepo.findById(responsesdto.getOptionId());
		if (optionalOption.isPresent()) {
			Options option = optionalOption.get();
			responses.setResponseAnswer(option.getOptions());

		} else {
			throw new NotFoundException();
		}

		Optional<Questions> optionalQuestion = qnRepo.findById(opt);
		if (optionalQuestion.isPresent()) {
			Questions question = optionalQuestion.get();
			responses.setResponseQuestion(question.getQuestions());
		} else {
			throw new NotFoundException();
		}

		rsRepo.save(responses);

		return true;

	}

	public boolean update(Responses responses) {
		rsRepo.save(responses);
		return true;
	}

	public boolean delete(long reponseId) {
		rsRepo.deleteById(reponseId);
		return true;
	}

	public List<Responses> getAllReponsesDetails() {
		Iterator<Responses> it = rsRepo.findAll().iterator();
		ArrayList<Responses> list = new ArrayList<>();
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}

	public List<Responses> getAllReponses(long surveyId) {
		Iterator<ResponseDetails> reDIt = resDetailRepo.findBySruveyId(surveyId).iterator();
		ArrayList<Responses> list = new ArrayList<>();
		while (reDIt.hasNext()) {
			ResponseDetails responseDetail = reDIt.next();
			Iterator<Responses> reIt = rsRepo.findByResponseDetailId(responseDetail.getResponseDetailId()).iterator();
			while (reIt.hasNext()) {
				list.add(reIt.next());
			}
		}
		return list;
	}

}
