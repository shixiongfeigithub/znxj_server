package com.niule.znxj.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.niule.znxj.web.dao.SendemailMapper;
import com.niule.znxj.web.model.Sendemail;
import com.niule.znxj.web.model.SendemailExample;
import com.niule.znxj.web.service.SendEmailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by administor on 2017/6/21.
 */
@Service
public class SendEmailServiceImpl implements SendEmailService{
    @Resource
    private SendemailMapper sendemailMapper;

    @Override
    public int addSendEmail(Sendemail sendemail) {
        return sendemailMapper.insert(sendemail);
    }

    @Override
    public List<Sendemail> showSendEmail(int page,int size) {
        PageHelper.startPage(page,size);
        SendemailExample sendemailExample=new SendemailExample();
        return sendemailMapper.selectByExample(sendemailExample);
    }

    @Override
    public Sendemail showSendById(Long id) {
        return sendemailMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updSendEmail(Sendemail sendemail) {
        return sendemailMapper.updateByPrimaryKeySelective(sendemail);
    }

    @Override
    public int delSendEmail(Long id) {
        return sendemailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Sendemail> selectByExample(SendemailExample example) {
        return sendemailMapper.selectByExample(example);
    }
}
