package org.ricardo.school_system.services;

import java.util.List;
import javax.transaction.Transactional;
import org.ricardo.school_system.assemblers.DegreeSubjectBundle;
import org.ricardo.school_system.daos.DegreeDao;
import org.ricardo.school_system.daos.SubjectDao;
import org.ricardo.school_system.entities.Degree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DegreeService extends GenericService<Degree> {

	@Autowired
	private DegreeDao degreeDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Override
	public Degree add(Degree degree) {	
		return null;
	}
	
	@Transactional
	public Degree addDegreeWithSubjects(DegreeSubjectBundle degreeSubjectBundle){
		
		Degree degree = new Degree(degreeSubjectBundle.getDegreeName());
		
		for(int subjectId : degreeSubjectBundle.getSubjectIds()) {
			degree.addSubject(subjectDao.getById(subjectId));
		}
		
		degreeDao.add(degree);
		
		return degree;
	}

	@Override
	@Transactional
	public List<Degree> getAll() {
		return degreeDao.getAll();
	}

	@Override
	public Degree getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Degree getByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Degree> getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Degree update(Degree degree) {
		// TODO Auto-generated method stub
		return null;
	}

}
