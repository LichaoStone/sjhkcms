package com.br.ott.common.util.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PatternMatchUtils;

/**
 * 读/写动态数据库选择处理器 通过AOP切面实现读/写选择
 * 
 * @author 陈登民
 * 
 */
// @Component
// @Aspect
public class ReadWriteDataSourceProcessor /* implements BeanPostProcessor */{
	private static final Logger log = LoggerFactory
			.getLogger(ReadWriteDataSourceProcessor.class);

	@Pointcut("execution (* com.br.ott.client.*.service.*Service.*(..))")
	public void pointcut() {
	}

	// 方法执行的前后调用
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		try {
			determineReadOrWriteDB(point.getSignature().getName());
			Object object = point.proceed();
			return object;
		} finally {
			ReadWriteDataSourceDecision.reset();
		}
	}

	// 方法运行出现异常时调用
	@AfterThrowing(pointcut = "pointcut()", throwing = "ex")
	public void afterThrowing(Exception ex) {
		log.error("afterThrowing", ex);
	}

	public void determineReadOrWriteDB(String methodName) {
		if (isChoiceReadDB(methodName)) {
			ReadWriteDataSourceDecision.markRead();
		} else {
			ReadWriteDataSourceDecision.markWrite();
		}
	}

	private boolean isChoiceReadDB(String methodName) {
		// 读库匹配
		String[] mappedNames = { "find*", "query*", "check*", "get*", "count*",
				"find*", "list*", "build*" };

		Boolean isForceChoiceRead = false;
		for (String mappedName : mappedNames) {
			if (isMatch(methodName, mappedName)) {
				isForceChoiceRead = true;
				break;
			}
		}

		// 表示强制选择 读 库
		if (isForceChoiceRead == Boolean.TRUE) {
			return true;
		}

		// 如果之前选择了写库 现在还选择 写库
		if (ReadWriteDataSourceDecision.isChoiceWrite()) {
			return false;
		}

		// 表示应该选择写库
		if (isForceChoiceRead == Boolean.FALSE) {
			return false;
		}
		// 默认选择 写库
		return false;
	}

	protected boolean isMatch(String methodName, String mappedName) {
		return PatternMatchUtils.simpleMatch(mappedName, methodName);
	}

}
