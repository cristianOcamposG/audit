package com.aiprocess.auditejb.facade;



import com.process.auditabstrac.AuditAbstrac;
import com.process.auditexception.AuditEJBException;
import com.process.entity.Company;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author crixx
 */
@Stateless
public class CompanyFacade extends AuditAbstrac<Company> {


    public CompanyFacade() {
        super(Company.class);
    }
    Date Fecha;
    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(String f) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Fecha = sdf.parse(f);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getSequence() throws AuditEJBException {

        try {
            String sql = "SELECT NEXTVAL('base.company_id_company_seq')";
            Query q = getEntityManager().createNativeQuery(sql);
            int newId = Integer.valueOf(q.getSingleResult().toString());

            //  log.info("Se obtuvo corretamente la sequencia de 'base.persona', new_id: {}", newId);
            return newId;
        } catch (Exception ex) {
            throw new AuditEJBException("No se pudo obtener la sequencia de base.persona.", ex);
        }
    }

    public Company findCompanyByRuc(String ruc) throws AuditEJBException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select c.* ");
            sb.append("  from base.company c");
            sb.append(" where c.ruc = ?1  and c.status = true ");

            Query q = getEntityManager().createNativeQuery(sb.toString(), Company.class);
            q.setParameter(1, ruc == null || ruc.trim().equals("") ? null : ruc.trim());
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List l = q.getResultList();

            if (l.isEmpty()) {
                return null;
            } else {
                return (Company) l.get(0);
            }

        } catch (Exception ex) {
            throw new AuditEJBException("No se pudo obtener la compañia.", ex);
        }
    }

    public List<Company> findCompany() throws AuditEJBException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select c.* ");
            sb.append("  from base.company c");
            System.out.println("em --> " + getEntityManager());
            Query q = getEntityManager().createNativeQuery(sb.toString(), Company.class);
         
            List l = q.getResultList();
                 System.out.println("este hace la lista "+ l);

            if (l.isEmpty()) {
                return null;
            } else {
                return l;
            }

        } catch (Exception ex) {
            throw new AuditEJBException("No se pudo obtener la moneda.", ex);
        }
    }


    public void createCompany(String name, String ruc, String phone, String address) throws AuditEJBException {

        try {
            Company newCompany = new Company();
            newCompany.setName(name);
            newCompany.setRuc(ruc);
            newCompany.setDateCreate(Fecha);
            newCompany.setPhone(phone);
            newCompany.setAddress(address);
            newCompany.setStatus(Boolean.TRUE);

            create(newCompany);

        } catch (Exception e) {
          

            throw new AuditEJBException("Error al crear company.", e);
        }
    }

    public void editComapany(String ruc, String phone, String address) throws AuditEJBException {
        Company company = findCompanyByRuc(ruc);
        company.setDateModified(Fecha);
        company.setPhone(phone);
        company.setAddress(address);
        edit(company);
        refresh(company);
    }

    public void editComapanystatus(String ruc) throws AuditEJBException {
        Company company = findCompanyByRuc(ruc);
        company.setStatus(Boolean.FALSE);
        edit(company);
        refresh(company);
    }
    
    public void updatecompany(String ruc, String phone, String address) throws AuditEJBException {
        int rowsUpdated = 0;

        try {
            StringBuilder sb = new StringBuilder();

            sb.append(" UPDATE base.company  ");
            sb.append(" SET  date_modified=?1 , phone=?2, address=?3 ");
            sb.append(" WHERE ruc=?4 ");
           

            Query q = getEntityManager().createNativeQuery(sb.toString());
            q.setParameter(1, Fecha );
            q.setParameter(2, phone);
            q.setParameter(3, address);
            q.setParameter(4, ruc);
            rowsUpdated = q.executeUpdate();

            if(rowsUpdated != 1){
                throw new AuditEJBException("No se pudo actualizar la compañia.");
            }else{
               // log.info("Se actualizo exitosamente el parámetro de sistema.");
                System.out.println("exito");
            }

        } catch (Exception ex) {
            if(ex instanceof AuditEJBException) throw (AuditEJBException) ex;

            throw new AuditEJBException("No se pudo actualizar los parametros", ex);
        }
    }
    
     public void updateCompanyStatus(String ruc) throws AuditEJBException {
        int rowsUpdated = 0;

        try {
            StringBuilder sb = new StringBuilder();

            sb.append(" UPDATE base.company");
            sb.append(" SET status= false");
            sb.append(" WHERE ruc=?1 ");
           

            Query q = getEntityManager().createNativeQuery(sb.toString());
            q.setParameter(1, ruc );
           
            rowsUpdated = q.executeUpdate();

            if(rowsUpdated != 1){
                throw new AuditEJBException("No se pudo actualizar el status la compañia.");
            }else{
               // log.info("Se actualizo exitosamente el parámetro de sistema.");
                System.out.println("exito");
            }

        } catch (Exception ex) {
            if(ex instanceof AuditEJBException) throw (AuditEJBException) ex;

            throw new AuditEJBException("No se pudo actualizarel status", ex);
        }
    }

      
}
