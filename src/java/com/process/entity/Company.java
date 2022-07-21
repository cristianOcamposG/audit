/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.process.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author crixx
 */
@Entity
@Table(name = "company")
@NamedQueries({
    @NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c")})
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ruc")
    private String ruc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
    @Column(name = "date_modified")
    @Temporal(TemporalType.DATE)
    private Date dateModified;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address")
    private String address;
    @Size(max = 255)
    @Column(name = "code_company")
    private String codeCompany;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @OneToMany(mappedBy = "idCompany")
    private List<Area> areaList;
    @OneToMany(mappedBy = "company")
    private List<Auditor> auditorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<UserRol> userRolList;
    @JoinColumn(name = "user_modified", referencedColumnName = "id_user")
    @ManyToOne
    private User userModified;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompany")
    private List<BranchOffice> branchOfficeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompany")
    private List<User> userList;
    @OneToMany(mappedBy = "idCompany")
    private List<DetailArea> detailAreaList;

    public Company() {
    }

    public Company(Integer id) {
        this.id = id;
    }

    public Company(Integer id, String name, String ruc, Date dateCreate, String phone, String address, boolean status) {
        this.id = id;
        this.name = name;
        this.ruc = ruc;
        this.dateCreate = dateCreate;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCodeCompany() {
        return codeCompany;
    }

    public void setCodeCompany(String codeCompany) {
        this.codeCompany = codeCompany;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public List<Auditor> getAuditorList() {
        return auditorList;
    }

    public void setAuditorList(List<Auditor> auditorList) {
        this.auditorList = auditorList;
    }

    public List<UserRol> getUserRolList() {
        return userRolList;
    }

    public void setUserRolList(List<UserRol> userRolList) {
        this.userRolList = userRolList;
    }

    public User getUserModified() {
        return userModified;
    }

    public void setUserModified(User userModified) {
        this.userModified = userModified;
    }

    public List<BranchOffice> getBranchOfficeList() {
        return branchOfficeList;
    }

    public void setBranchOfficeList(List<BranchOffice> branchOfficeList) {
        this.branchOfficeList = branchOfficeList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<DetailArea> getDetailAreaList() {
        return detailAreaList;
    }

    public void setDetailAreaList(List<DetailArea> detailAreaList) {
        this.detailAreaList = detailAreaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.process.entity.Company[ id=" + id + " ]";
    }
    
}
