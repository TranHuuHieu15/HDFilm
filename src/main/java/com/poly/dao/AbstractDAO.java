package com.poly.dao;

import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.poly.util.JpaUtil;

public class AbstractDAO<T> {
	public static final EntityManager entityManager = JpaUtil.getEntityManager();

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}

	public T findById(Class<T> clazz, Integer id) {
		return entityManager.find(clazz, id);
	}

	// nếu existIsActive = true thì truyền isActive = 1
	// ngược lại thì ko truyền vào
	public List<T> findAll(Class<T> clazz, boolean existIsActive) {
		String entiryName = clazz.getSimpleName(); // lấy được tess class(User, Video, History..)
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o FROM ").append(entiryName).append(" o");

		if (existIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}

		TypedQuery<T> query = entityManager.createQuery(sql.toString(), clazz);
		return query.getResultList();
	}

	public List<T> findAll(Class<T> clazz, boolean existIsActive, int pageNumber, int pageSize) {
		String entiryName = clazz.getSimpleName(); // lấy được tên class(User, Video, History..)
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o FROM ").append(entiryName).append(" o");

		if (existIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}

		TypedQuery<T> query = entityManager.createQuery(sql.toString(), clazz);
		/*
		 * @pageSize: số lượng phần tử trong một trang
		 * 
		 * @pageNumber: Số lượng trang 5 phần tử mà một trang muốn chứa 2 phần tử thì ->
		 * tổng số trang là 3 - trang 1: 2 phần tử - trang 2 : 2 phần tử - trang 3: 1
		 * phần tử muốn lấy các phần tử ở trang thứ 2 => pageNumber = 2, pageSize = 2 1
		 * * 2 = 2 -> bắt đầu lấy từ phần tử thứ 2 và lấy tổng cộng 2 phần tử
		 */
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	// select o from user o where o.username = ?0 and o.password = ?1;
	public T findOne(Class<T> clazz, String sql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(sql, clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public List<T> findMany(Class<T> clazz, String sql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(sql, clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findManyByNativeQuery(String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}

	public T create(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			System.out.println("Create entity success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Can't insert entity! " + entity.getClass().getSimpleName() + " to DB");
			throw new RuntimeException(e);
		}
	}

	public T update(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
			System.out.println("Update entity success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Can't update entity!" + entity.getClass().getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public T delete(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
			System.out.println("Delete entity success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Can't delete entity!" + entity.getClass().getSimpleName());
			throw new RuntimeException(e);
		}
	}

	// hàm gọi đến sp
	@SuppressWarnings("unchecked")
	public List<T> callStored(String nameStored, Map<String, Object> params) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(nameStored);
		params.forEach((key, value) -> query.setParameter(key, value));
		return (List<T>) query.getResultList();
	}
}
