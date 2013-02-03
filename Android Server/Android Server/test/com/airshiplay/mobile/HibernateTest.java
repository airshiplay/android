/**
 * 
 */
package com.airshiplay.mobile;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.airshiplay.mobile.db.HibernateUtil;
import com.airshiplay.mobile.model.Soft;
import com.airshiplay.mobile.model.SoftGroup;
import com.airshiplay.mobile.model.User;

/**
 * @author airshiplay
 * @Create Date 2013-2-3
 * @version 1.0
 * @since 1.0
 */
public class HibernateTest {
	public static void main(String[] args) {
		SessionFactory factory=HibernateUtil.getSessionFactory();
		Session session=factory.openSession();
		User user = new User();
		user.setName("guest");
		user.setPwd("guest");
		SoftGroup group = new SoftGroup();
		group.setName("”Œœ∑4");
		Soft soft = new Soft();
		soft.setName("÷«ª€≥« –47777");
		group.getSofts().add(soft);
		Soft e= new Soft();
		e.setName("4444-e777");
		
		
		session.beginTransaction();
		group=(SoftGroup) session.get(SoftGroup.class, "402881e83ca00fb8013ca00fcbf20002");
		//session.save(group);
		soft.getGroups().add(group);
		e.getGroups().add(group);
		group.getSofts().add(e);
		group.getSofts().add(soft);
		//soft.setGroup(group);
		session.save(e);
		session.save(soft);
		session.save(group);
		session.getTransaction().commit();
		session.close();
	}
}
