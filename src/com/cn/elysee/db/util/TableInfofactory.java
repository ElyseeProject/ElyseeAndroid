package com.cn.elysee.db.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.cn.elysee.db.entity.PKProperyEntity;
import com.cn.elysee.db.entity.PropertyEntity;
import com.cn.elysee.db.entity.TableInfoEntity;
import com.cn.elysee.exception.DBException;

/**
 * @author hzx 
 * 2014��4��18��
 * @Description ���ݿ������
 * @version V1.0
 */
public class TableInfofactory
{
	/**
	 * ����Ϊ��������ϢΪֵ��HashMap
	 */
	private static final HashMap<String, TableInfoEntity> tableInfoEntityMap = new HashMap<String, TableInfoEntity>();

	private TableInfofactory()
	{

	}

	private static TableInfofactory instance;

	/**
	 * ������ݿ����
	 * 
	 * @return ���ݿ����
	 */
	public static TableInfofactory getInstance()
	{
		if (instance == null)
		{
			instance = new TableInfofactory();
		}
		return instance;
	}

	/**
	 * ��ñ���Ϣ
	 * 
	 * @param clazz
	 *            ʵ������
	 * @return ����Ϣ
	 * @throws TADBException
	 */
	public TableInfoEntity getTableInfoEntity(Class<?> clazz)
			throws DBException
	{
		if (clazz == null)
			throw new DBException("����Ϣ��ȡʧ�ܣ�ӦΪclassΪnull");
		TableInfoEntity tableInfoEntity = tableInfoEntityMap.get(clazz
				.getName());
		if (tableInfoEntity == null)
		{
			tableInfoEntity = new TableInfoEntity();
			tableInfoEntity.setTableName(DBUtils.getTableName(clazz));
			tableInfoEntity.setClassName(clazz.getName());
			Field idField = DBUtils.getPrimaryKeyField(clazz);
			if (idField != null)
			{
				PKProperyEntity pkProperyEntity = new PKProperyEntity();
				pkProperyEntity.setColumnName(DBUtils
						.getColumnByField(idField));
				pkProperyEntity.setName(idField.getName());
				pkProperyEntity.setType(idField.getType());
				pkProperyEntity.setAutoIncrement(DBUtils
						.isAutoIncrement(idField));
				tableInfoEntity.setPkProperyEntity(pkProperyEntity);
			}
			else
			{
				tableInfoEntity.setPkProperyEntity(null);
			}
			List<PropertyEntity> propertyList = DBUtils
					.getPropertyList(clazz);
			if (propertyList != null)
			{
				tableInfoEntity.setPropertieArrayList(propertyList);
			}

			tableInfoEntityMap.put(clazz.getName(), tableInfoEntity);
		}
		if (tableInfoEntity == null
				|| tableInfoEntity.getPropertieArrayList() == null
				|| tableInfoEntity.getPropertieArrayList().size() == 0) { throw new DBException(
				"���ܴ���+" + clazz + "�ı���Ϣ"); }
		return tableInfoEntity;
	}
}
