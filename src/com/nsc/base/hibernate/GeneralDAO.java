package com.nsc.base.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.NamedSQLQueryDefinition;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * 通用数据库查询工具类
 * 
 * 此类通过对BaseDAO的扩展，进一步整合数据库操作功能。保证对外风格统一，简单明确。
 * 
 * @author bs-team
 * 
 * @date Oct 19, 2010 9:57:52 AM
 * @version
 */
@SuppressWarnings("all")
public class GeneralDAO extends BaseDAO {

	/**
	 * 利用Entity对象获取记录集
	 * 
	 * @param entity
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List getResult(Object entity) {
		return super.getHibernateTemplate().findByExample(entity);
	}

	/**
	 * 利用Entity对象获取记录集
	 * 
	 * @param entity
	 *            --------------------
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List getResultPage(Object entity, int firstResult, int maxResults) {

		List list = super.getHibernateTemplate().findByExample(entity,
				firstResult, maxResults);

		return list;
	}

	/**
	 * 根据ID查询实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public Object findByID(Class<?> clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	/**
	 * 批量删除Entity
	 * 
	 * @param entity
	 */
	public void deleteAll(String queryString) {
		super.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(queryString).executeUpdate();
	}

	/**
	 * 利用HQL的名称和数组参数查询
	 * 
	 * @param hqlName
	 * @param params
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List getResult(String hqlName, Object[] params) {
		return findResultBySQLName(hqlName, params);
	}

	/**
	 * 利用HQL的名称和数组参数查询，将结果按给定CLASS转换
	 * 
	 * @param hqlName
	 * @param params
	 * @param class_
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List getResult(String hqlName, Object[] params, Class<?> class_) {
		return findResultBySQLName(hqlName, params, class_);
	}

	/**
	 * 利用HQL的名称和HashMap参数查询，将结果按给定CLASS转换
	 * 
	 * @param hqlName
	 * @param paraMap
	 * @return
	 */
	public List getResult(String hqlName, Map<String, Object> paraMap) {
		return findResultBySQLName(hqlName, paraMap);
	}

	/**
	 * 分页查询 通过HQL名称到配置文件中获取HQL，然后进行查询。 HQL中可以以 ?设置参数 :list参数通过列表设置
	 * :in参数通过数组设置，参数通过Map<String, Parameter>传入。 通过设置开始记录和结束记录缩小查询范围
	 * 
	 * @param hqlName
	 * @param paraMap
	 * @param firstResult
	 * @param maxResults
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List getResult(final String hqlName,
			final Map<String, Object> paraMap, int firstResult, int maxResults)
			throws Exception {
		return findResultBySQLName(hqlName, paraMap, firstResult, maxResults);
	}

	/**
	 * 分页查询 通过HQL名称到配置文件中获取HQL，然后进行查询。 HQL中可以以 ?设置参数 :list参数通过列表设置
	 * :in参数通过数组设置，参数通过Map<String, Parameter>传入。 通过设置开始记录和结束记录缩小查询范围
	 * 
	 * @param hqlName
	 * @param paraMap
	 * @param firstResult
	 * @param maxResults
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List getResult(final String hqlName, final Object[] obj,
			int firstResult, int maxResults) throws Exception {
		return findResultBySQLName(hqlName, obj, firstResult, maxResults);
	}

	public long getResultCount(final String hqlName,
			final Map<String, Object> paraMap) {
		return super.findCountBySQLName(hqlName, paraMap);
	}

	public long getResultCount(final String hqlName, final Object[] obj) {
		return super.findCountBySQLName(hqlName, obj);
	}

	/**
	 * 利用HQL的名称进行查询
	 * 
	 * @param hqlName
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List getResult(String hqlName) {
		return findResultBySQLName(hqlName, new Object[] {});
	}

	/**
	 * 利用传入的HQL进行查询
	 * 
	 * @param hql
	 * @return 记录集
	 */
	@SuppressWarnings("unchecked")
	public List queryByHQL(String hql) {
		return super.getHibernateTemplate().find(hql);
	}

	/**
	 * 查询传入的Entity对象
	 * 
	 * @param entity
	 */
	public void refresh(Object entity) {
		super.getHibernateTemplate().refresh(entity);
	}

	/**
	 * 融合Entity，根据数据库中是否有记录，自动选择更新或插入
	 * 
	 * @param entity
	 */
	public void merge(Object entity) {
		super.getHibernateTemplate().merge(entity);
	}

	/**
	 * 增加Entity到数据库
	 * 
	 * @param entity
	 */
	public void add(Object entity) {
		super.getHibernateTemplate().save(entity);
	}

	/**
	 * 更新Entity
	 * 
	 * @param entity
	 */
	public void modify(Object entity) {
		super.getHibernateTemplate().update(entity);
	}

	/**
	 * 删除Entity
	 * 
	 * @param entity
	 */
	public void delete(Object entity) {
		super.getHibernateTemplate().delete(entity);
	}

	public List queryByString(String queryString) {
		return super.getHibernateTemplate().find(queryString);
	}

	public String getQueryString(String queryName) {
		SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) super
				.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.getSessionFactory();
		NamedSQLQueryDefinition nsqd = sessionFactoryImpl
				.getNamedSQLQuery(queryName);

		return nsqd.getQueryString();
	}

	public List<Object[]> queryByNativeSQL(String queryString) {
		return super.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(queryString).list();
	}

	public List<?> queryByNativeSQL(String queryString,
			final Map<String, Object> paraMap) {
		return super.findResultBySQL(queryString, paraMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryByNativeSQL2(final String queryString) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// native sql查询，返回list中是map<key:column,value:columnValue>
				return session.createSQLQuery(queryString)
						.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
						.list();
			}
		});
	}

	public Criteria createCriteria(Object entity) {
		return super.createCriteria(entity);
	}

	/**
	 * 使用本地SQL语句查询，返回实体类
	 * 
	 * @param queryString
	 *            SQL语句
	 * @param clazz
	 *            实体类的class
	 * @return
	 */
	public List queryByNativeSQLEntity(String queryString, Class clazz) {
		SQLQuery query = super.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(queryString).addEntity(
						clazz);

		return query == null ? null : query.list();

	}

	/**
	 * 查询聚合函数的值
	 * 
	 * @param queryString
	 * @param strScalar
	 *            聚合函数的别名
	 * @return
	 */
	public Integer queryByNativeSQLMAX(String queryString, String strScalar) {
		return (Integer) super.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createSQLQuery(queryString).addScalar(
						strScalar, Hibernate.INTEGER).uniqueResult();
	}

}
