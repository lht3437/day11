package com.xiaoshu.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.PersonVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class PersonService {

	@Autowired
	PersonMapper personMapper;

	@Autowired
	CompanyMapper companyMapper;

	public PageInfo<PersonVo> findList(PersonVo personVo, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<PersonVo> list=personMapper.findList(personVo);
		return new PageInfo<>(list);
	}



	public List<Company> findAll() {
		// TODO Auto-generated method stub
		return companyMapper.selectAll();
	}


	public void addPerson(Person person) {
		// TODO Auto-generated method stub
		person.setCreateTime(new Date());
		personMapper.insert(person);
	}



	public void updatePerson(Person person) {
		// TODO Auto-generated method stub
		
		personMapper.updateByPrimaryKeySelective(person);
	}



	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub
		personMapper.deleteByPrimaryKey(id);
	}



	public void personFile(MultipartFile personFile) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
		Workbook workbook = WorkbookFactory.create(personFile.getInputStream());
		Sheet sheet = workbook.getSheetAt(0);
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 0; i <lastRowNum; i++) {
			Row row = sheet.getRow(i+1);
			String expressName = row.getCell(0).toString();
			
			String sex = row.getCell(1).toString();
			
			String expressTrait = row.getCell(2).toString();
			Date entryTime = row.getCell(3).getDateCellValue();
			String cname = row.getCell(4).toString();
			
			if(!cname.equals("京东")){
				
				continue;
			}
			
			Person p =new Person();
			p.setExpressName(expressName);
			p.setSex(sex);
			p.setExpressTrait(expressTrait);
			p.setEntryTime(entryTime);
			
			int cid = findCompany(cname);
			
			p.setExpressTypeId(cid);
			
			p.setCreateTime(new Date());
			personMapper.insert(p);
		}
		
		
	}


//参数是公司名称 cname
	public int findCompany(String cname) {
		//创建查询对象
		Company param=new Company();
		//把cname存入查询对象
		param.setExpressName(cname);
		//根据查询对象到数据库找到公司名相同的公司对象 用company接受找到的对象
		Company company = companyMapper.selectOne(param);
		
		
		if(company==null){
			param.setCreateTime(new Date());
			param.setStatus("快");
			
			companyMapper.insertCompany(param);
			
			System.out.println(param);
			company = param;
		}
		
		return company.getId();
	}



	public List<PersonVo> findLog(PersonVo personVo) {
		// TODO Auto-generated method stub
		return personMapper.findList(personVo);
	}



	public Person existUserWithUserName(String expressName) {
		// TODO Auto-generated method stub
		
		List<Person> userList = personMapper.findByName(expressName);
		return userList.isEmpty()?null:userList.get(0);
	};



}
