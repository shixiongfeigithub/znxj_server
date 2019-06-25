package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Sendemail;
import com.niule.znxj.web.model.SendemailExample;

import java.util.List;

/**
 * Created by administor on 2017/6/21.
 */
public interface SendEmailService {
    int addSendEmail(Sendemail sendemail);
    List<Sendemail> showSendEmail(int page,int size);
    Sendemail showSendById(Long id);
    int updSendEmail(Sendemail sendemail);
    int delSendEmail(Long id);
    List<Sendemail> selectByExample(SendemailExample example);
}
