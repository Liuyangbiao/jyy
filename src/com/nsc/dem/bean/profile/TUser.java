package com.nsc.dem.bean.profile;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.nsc.base.annotation.IdentifierFieldAnnotation;
import com.nsc.dem.bean.system.TUnit;

/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
@IdentifierFieldAnnotation(identifier = "loginId")
@SuppressWarnings(value = { "serial", "unchecked" })
public class TUser implements java.io.Serializable {

	// Fields
    //
	private String loginId;
	private TUnit TUnit;
	private TRole TRole;
	private Long password;
	private String name;
	private String duty;
	private Timestamp loginTime;
	private Timestamp lastLoginTime;
	private Long loginCount;
	private Boolean isValid;
	private String creator;
	private Timestamp createDate;
	private String telephone;
	private Set TRoleProfiles = new HashSet(0);
	private Set TRoleTrees = new HashSet(0);
	private Set TProfileTemps = new HashSet(0);
	private Set TUnits = new HashSet(0);
	private Set TTenders = new HashSet(0);
	private Set TDictionaries = new HashSet(0);
	private Set TNodeDefs = new HashSet(0);
	private Set TUserProfilesForUserId = new HashSet(0);
	private Set TTreeDefs = new HashSet(0);
	private Set TOperateLogs = new HashSet(0);
	private Set TDocTypes = new HashSet(0);
	private Set TLogFilesForBackupOperator = new HashSet(0);
	private Set TReports = new HashSet(0);
	private Set TProjects = new HashSet(0);
	private Set TUserProfilesForGrantUserId = new HashSet(0);
	private Set TLogFilesForDeleteOperator = new HashSet(0);
	private Set TProfiles = new HashSet(0);
	private Set TTempletes = new HashSet(0);
	private Set TComponents = new HashSet(0);
	private Set TDocs = new HashSet(0);
	//标识是否是远程登录
	private String isLocal;
	private String romoteLogo;
	// Constructors

	/** default constructor */
	public TUser() {
	}

	
	public String getRomoteLogo() {
		return romoteLogo;
	}



	public void setRomoteLogo(String romoteLogo) {
		this.romoteLogo = romoteLogo;
	}



	public String getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(String isLocal) {
		this.isLocal = isLocal;
	}

	/** minimal constructor */
	public TUser(String loginId, TUnit TUnit, TRole TRole, Long password,
			String name, String duty, Long loginCount, Boolean isValid,
			String creator, Timestamp createDate, String telephone) {
		this.loginId = loginId;
		this.TUnit = TUnit;
		this.TRole = TRole;
		this.password = password;
		this.name = name;
		this.duty = duty;
		this.loginCount = loginCount;
		this.isValid = isValid;
		this.creator = creator;
		this.createDate = createDate;
		this.telephone = telephone;
	}

	/** full constructor */
	public TUser(String loginId, TUnit TUnit, TRole TRole, Long password,
			String name, String duty, Timestamp loginTime,
			Timestamp lastLoginTime, Long loginCount, Boolean isValid,
			String creator, Timestamp createDate, String telephone,
			Set TRoleProfiles, Set TRoleTrees, Set TProfileTemps, Set TUnits,
			Set TTenders, Set TDictionaries, Set TNodeDefs,
			Set TUserProfilesForUserId, Set TTreeDefs, Set TOperateLogs,
			Set TDocTypes, Set TLogFilesForBackupOperator, Set TReports,
			Set TProjects, Set TUserProfilesForGrantUserId,
			Set TLogFilesForDeleteOperator, Set TProfiles, Set TTempletes,
			Set TComponents, Set TDocs) {
		this.loginId = loginId;
		this.TUnit = TUnit;
		this.TRole = TRole;
		this.password = password;
		this.name = name;
		this.duty = duty;
		this.loginTime = loginTime;
		this.lastLoginTime = lastLoginTime;
		this.loginCount = loginCount;
		this.isValid = isValid;
		this.creator = creator;
		this.createDate = createDate;
		this.telephone = telephone;
		this.TRoleProfiles = TRoleProfiles;
		this.TRoleTrees = TRoleTrees;
		this.TProfileTemps = TProfileTemps;
		this.TUnits = TUnits;
		this.TTenders = TTenders;
		this.TDictionaries = TDictionaries;
		this.TNodeDefs = TNodeDefs;
		this.TUserProfilesForUserId = TUserProfilesForUserId;
		this.TTreeDefs = TTreeDefs;
		this.TOperateLogs = TOperateLogs;
		this.TDocTypes = TDocTypes;
		this.TLogFilesForBackupOperator = TLogFilesForBackupOperator;
		this.TReports = TReports;
		this.TProjects = TProjects;
		this.TUserProfilesForGrantUserId = TUserProfilesForGrantUserId;
		this.TLogFilesForDeleteOperator = TLogFilesForDeleteOperator;
		this.TProfiles = TProfiles;
		this.TTempletes = TTempletes;
		this.TComponents = TComponents;
		this.TDocs = TDocs;
	}

	// Property accessors
	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public TUnit getTUnit() {
		return this.TUnit;
	}

	public void setTUnit(TUnit TUnit) {
		this.TUnit = TUnit;
	}

	public TRole getTRole() {
		return this.TRole;
	}

	public void setTRole(TRole TRole) {
		this.TRole = TRole;
	}

	public Long getPassword() {
		return this.password;
	}

	public void setPassword(Long password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Timestamp getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Long loginCount) {
		this.loginCount = loginCount;
	}

	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Set getTRoleProfiles() {
		return this.TRoleProfiles;
	}

	public void setTRoleProfiles(Set TRoleProfiles) {
		this.TRoleProfiles = TRoleProfiles;
	}

	public Set getTRoleTrees() {
		return this.TRoleTrees;
	}

	public void setTRoleTrees(Set TRoleTrees) {
		this.TRoleTrees = TRoleTrees;
	}

	public Set getTProfileTemps() {
		return this.TProfileTemps;
	}

	public void setTProfileTemps(Set TProfileTemps) {
		this.TProfileTemps = TProfileTemps;
	}

	public Set getTUnits() {
		return this.TUnits;
	}

	public void setTUnits(Set TUnits) {
		this.TUnits = TUnits;
	}

	public Set getTTenders() {
		return this.TTenders;
	}

	public void setTTenders(Set TTenders) {
		this.TTenders = TTenders;
	}

	public Set getTDictionaries() {
		return this.TDictionaries;
	}

	public void setTDictionaries(Set TDictionaries) {
		this.TDictionaries = TDictionaries;
	}

	public Set getTNodeDefs() {
		return this.TNodeDefs;
	}

	public void setTNodeDefs(Set TNodeDefs) {
		this.TNodeDefs = TNodeDefs;
	}

	public Set getTUserProfilesForUserId() {
		return this.TUserProfilesForUserId;
	}

	public void setTUserProfilesForUserId(Set TUserProfilesForUserId) {
		this.TUserProfilesForUserId = TUserProfilesForUserId;
	}

	public Set getTTreeDefs() {
		return this.TTreeDefs;
	}

	public void setTTreeDefs(Set TTreeDefs) {
		this.TTreeDefs = TTreeDefs;
	}

	public Set getTOperateLogs() {
		return this.TOperateLogs;
	}

	public void setTOperateLogs(Set TOperateLogs) {
		this.TOperateLogs = TOperateLogs;
	}

	public Set getTDocTypes() {
		return this.TDocTypes;
	}

	public void setTDocTypes(Set TDocTypes) {
		this.TDocTypes = TDocTypes;
	}

	public Set getTLogFilesForBackupOperator() {
		return this.TLogFilesForBackupOperator;
	}

	public void setTLogFilesForBackupOperator(Set TLogFilesForBackupOperator) {
		this.TLogFilesForBackupOperator = TLogFilesForBackupOperator;
	}

	public Set getTReports() {
		return this.TReports;
	}

	public void setTReports(Set TReports) {
		this.TReports = TReports;
	}

	public Set getTProjects() {
		return this.TProjects;
	}

	public void setTProjects(Set TProjects) {
		this.TProjects = TProjects;
	}

	public Set getTUserProfilesForGrantUserId() {
		return this.TUserProfilesForGrantUserId;
	}

	public void setTUserProfilesForGrantUserId(Set TUserProfilesForGrantUserId) {
		this.TUserProfilesForGrantUserId = TUserProfilesForGrantUserId;
	}

	public Set getTLogFilesForDeleteOperator() {
		return this.TLogFilesForDeleteOperator;
	}

	public void setTLogFilesForDeleteOperator(Set TLogFilesForDeleteOperator) {
		this.TLogFilesForDeleteOperator = TLogFilesForDeleteOperator;
	}

	public Set getTProfiles() {
		return this.TProfiles;
	}

	public void setTProfiles(Set TProfiles) {
		this.TProfiles = TProfiles;
	}

	public Set getTTempletes() {
		return this.TTempletes;
	}

	public void setTTempletes(Set TTempletes) {
		this.TTempletes = TTempletes;
	}

	public Set getTComponents() {
		return this.TComponents;
	}

	public void setTComponents(Set TComponents) {
		this.TComponents = TComponents;
	}

	public Set getTDocs() {
		return this.TDocs;
	}

	public void setTDocs(Set TDocs) {
		this.TDocs = TDocs;
	}

}