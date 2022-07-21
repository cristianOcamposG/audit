/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.process.auditabstrac;

import javax.faces.validator.Validator;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.validation.*;
import javax.validation.metadata.ConstraintDescriptor;
//import static org.eclipse.persistence.jpa.JpaHelper.getEntityManager;


/**
 *
 * @author crixx
 */
public abstract class AuditAbstrac<T> {

    
      @PersistenceContext(unitName = "aiprocess_AuditoriaEJB")
    private EntityManager EntityManager;
    
      
   // protected abstract EntityManager getEntityManager();

    public EntityManager getEntityManager() {
        return EntityManager;
    }

  
      
    private Class<T> entityClass;

    private Validator validator;
    
    public AuditAbstrac(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void create(T entity) throws ConstraintViolationException {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public void edit(T entity) throws ConstraintViolationException {
        
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public void refresh(T entity) throws ConstraintViolationException {
        getEntityManager().refresh(entity);
    }
    
     public List<T> findAllByActiveGeneric() throws Exception {
        return findAllByActiveGeneric(true, null);
    }

  
    public List<T> findAllByActiveGeneric(boolean onlyActive, String[] fieldsOrderBy) throws Exception{
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery q = cb.createQuery(entityClass);
            Root<T> root = q.from(entityClass);
            q.select(root);
            
            Path<Boolean> isActive = root.get("activo");
            //Expression<Boolean> paramOnlyActive = cb.parameter(Boolean.class);
            //q.where(cb.isTrue(isActive), cb.and(cb.isTrue(paramOnlyActive)));

            if (isActive == null) {
                throw new Exception("El campo Activo NO existe en la entidad " + entityClass.getName());
            }
            
            if (onlyActive) {
                q.where(cb.isTrue(isActive));
            }

            // Resolviendo el order by por varios campos, todos ascendentes :)
            List<Order> orders = null;
            if (fieldsOrderBy != null) {
                for (int i = 0; i < fieldsOrderBy.length; i++) {
                    if (fieldsOrderBy[i] != null && !fieldsOrderBy[i].trim().isEmpty()) {
                        if (i == 0) {
                            orders = new ArrayList<Order>();
                        }
                        
                        if (root.get(fieldsOrderBy[i]) != null) {
                            orders.add(cb.asc(root.get(fieldsOrderBy[i])));
                        } else {
                         //   log.warn("El campo {} no es atributo de la entidad {}, no se incluye en el order by.",
                           //         fieldsOrderBy[i],
                             //       entityClass.getSimpleName());
                        }
                    }
                }
            } else {
                // Se agrega el order by por descripción si hubiess
                Expression<String> desc = findDescriptionField(root);
                
                if (desc != null) {
                    orders.add(cb.asc(desc));
                }
            }
            
            if (orders != null && orders.size() > 0) {
                q.orderBy(orders);
            }
            
            return getEntityManager().createQuery(q).getResultList();
        } catch (Exception ex) {
            if (ex instanceof Exception) {
                throw (Exception) ex;
            }
            
            throw new Exception("No se pudo ejecutar el select genérico.", ex);
        }
    }
    
    private Expression<String> findDescriptionField(Root<T> root) {
        Expression<String> descField = root.get("description");
        
        if (descField == null) {
            descField = root.get("descripcion");
        }
        
        return descField;
    }
    
    public List<T> findAll() {
        return findAll(null);
    }
    
    public List<T> findAll(String[] fieldsOrderBy) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery q = cb.createQuery();
        Root<T> root = q.from(entityClass);
        q.select(root);

        // Resolviendo el order by por varios campos, todos ascendentes :)
        List<Order> orders = null;
        if (fieldsOrderBy != null) {
            for (int i = 0; i < fieldsOrderBy.length; i++) {
                if (fieldsOrderBy[i] != null && !fieldsOrderBy[i].trim().isEmpty()) {
                    if (i == 0) {
                        orders = new ArrayList<Order>();
                    }
                    if (root.get(fieldsOrderBy[i]) != null) {
                        orders.add(cb.asc(root.get(fieldsOrderBy[i])));
                    } else {
//                        log.warn("El campo {} no es atributo de la entidad {}, no se incluye en el order by.",
//                                fieldsOrderBy[i],
//                                entityClass.getSimpleName());
                    }
                }
            }
        }
        
        if (orders != null && orders.size() > 0) {
            q.orderBy(orders);
        }
        
        return getEntityManager().createQuery(q).getResultList();
    }
    
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        
        return q.getResultList();
    }
    
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
    
    public void detached(T t) {
        getEntityManager().detach(t);
    }
}

    
    


