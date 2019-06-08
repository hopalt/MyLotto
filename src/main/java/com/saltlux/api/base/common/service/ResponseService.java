package com.saltlux.api.base.common.service;

import java.util.List;

import com.saltlux.api.base.common.vo.CommonResult;
import com.saltlux.api.base.common.vo.ListResult;
import com.saltlux.api.base.common.vo.SingleResult;

import org.springframework.stereotype.Service;

@Service
public class ResponseService{
  public enum CommonResponse{
    SUCCESS(0, "성공");

    int code;
    String msg;

    CommonResponse(int code, String msg){
      this.code = code;
      this.msg = msg;
    }

    public int getCode(){
      return code;
    }

    public String getMsg(){
      return msg;
    }
  }

  //단일 결과를 처리하는 메소드
  public <T> SingleResult<T> getSingleResult(T data){
    SingleResult<T> result = new SingleResult<>();
    result.setData(data);
    setSuccessResult(result);
    return result;
  }

  //다중 결과를 처리 하는 메소드
  public <T> ListResult<T> getListResult(List<T> list){
    ListResult<T> result = new ListResult<>();
    result.setList(list);
    setSuccessResult(result);
    return result;
  }
  //성공 결과만 처리하는 메소드
  public CommonResult getSuccessResult(){
    CommonResult result = new CommonResult();
    setSuccessResult(result);
    return result;
  }

  //실패 결과를 처리할 메소드
  public CommonResult getFailResult(int code, String msg){
    CommonResult result = new CommonResult();
    result.setSuccess(false);
    result.setCode(code);
    result.setMsg(msg);
    return result;
  }

  //성공 데이터를 셋팅해주는 메소드
  private void setSuccessResult(CommonResult result){
    result.setSuccess(true);
    result.setCode(CommonResponse.SUCCESS.getCode());
    result.setMsg(CommonResponse.SUCCESS.getMsg());
  }
}