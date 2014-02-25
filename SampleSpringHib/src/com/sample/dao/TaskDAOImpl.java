package com.sample.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.form.Task;

@Repository
public class TaskDAOImpl implements TaskDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addTask(Task task) {
		sessionFactory.getCurrentSession().save(task);

	}

	@Override
	public List<Task> listTask() {
		return sessionFactory.getCurrentSession().createQuery("from Task").list();
	}

	@Override
	public void removeTask(Integer id) {
		Task contact = (Task) sessionFactory.getCurrentSession().load(Task.class, id);
		if (null != contact) {
			sessionFactory.getCurrentSession().delete(contact);
		}
	}

	@Override
	public List<Task> getTasks(String email) {
		return sessionFactory.getCurrentSession().createQuery("from Task T where T.email='" + email +"'").list();
	}

}
