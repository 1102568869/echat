//package tech.washmore.easychat.common.autocomplete;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import tech.washmore.family.common.uc.LoginUserHolder;
//import tech.washmore.family.model.BookUserBase;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author Washmore
// * @version V1.0
// * @summary 自动注入creator和updater相关字段
// * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
// * @since 2018/3/15
// */
//@Aspect
//@Configuration
//public class AutoCompleter {
//    private static final List<String> AUTO_COMPLETE_CREATOR = Arrays.asList("creator", "creatorName", "createTime");
//    private static final List<String> AUTO_COMPLETE_UPDATER = Arrays.asList("updater", "updaterName", "updateTime");
//
//    @Autowired
//    private LoginUserHolder loginUserHolder;
//
//
//    /**
//     * 切面配置
//     */
//    @Pointcut("@annotation(tech.washmore.family.common.autocomplete.AutoComplete)")
//    public void pointCut() {
//    }
//
//    /**
//     * 切面环绕增强
//     *
//     * @param pjp
//     * @return
//     * @throws Throwable
//     */
//    @Around("pointCut()")
//    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
//        //获取被拦截的方法
//        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//        //获取被拦截的方法返回值类型
//        Class<?> resultType = method.getReturnType();
//        //获取被拦截的方法参数
//        Object[] args = pjp.getArgs();
//        if (ArrayUtils.getLength(args) != 1) {
//            return pjp.proceed(args);
//        }
//
//        Object onlyArg = args[0];
//
//        AutoComplete autoComplete = method.getDeclaredAnnotation(AutoComplete.class);
//
//        AutoCompleteType type = autoComplete.type();
//        AutoCompleteTarget target = autoComplete.target();
//
//        this.autoComplete(onlyArg, type, target);
//
//        return pjp.proceed(args);
//    }
//
//    //同时也提供显示调用的方法
//    public void autoCompleteDefault(Object onlyArg) {
//        this.autoComplete(onlyArg, AutoCompleteType.All, AutoCompleteTarget.CurrentLoginUser);
//    }
//
//    //同时也提供显示调用的方法
//    public void autoCompleteAdminDefault(Object onlyArg) {
//        this.autoComplete(onlyArg, AutoCompleteType.All, AutoCompleteTarget.Admin);
//    }
//
//    //同时也提供显示调用的方法
//    public void autoCompleteLoginUserAsOnlyUpdater(Object onlyArg) {
//        this.autoComplete(onlyArg, AutoCompleteType.OnlyUpdater, AutoCompleteTarget.CurrentLoginUser);
//    }
//
//    //同时也提供显示调用的方法
//    public void autoCompleteAdminAsOnlyUpdater(Object onlyArg) {
//        this.autoComplete(onlyArg, AutoCompleteType.OnlyUpdater, AutoCompleteTarget.Admin);
//    }
//
//    private void autoComplete(Object onlyArg, AutoCompleteType type, AutoCompleteTarget target) {
//        try {
//            Class<?> cls = onlyArg.getClass();
//            if (!cls.getPackage().getName().startsWith("tech.washmore.family.model")) {
//                return;
//            }
//
//            if (target == null) {
//                target = AutoCompleteTarget.CurrentLoginUser;
//            }
//            if (type == null) {
//                type = AutoCompleteType.All;
//            }
//            Long userCode = AutoCompleteTarget.Admin.getUserCode();
//            String userName = AutoCompleteTarget.Admin.getUserName();
//
//            if (target == AutoCompleteTarget.CurrentLoginUser) {
//                BookUserBase loginUser = loginUserHolder.getCurrentUser();
//                userCode = loginUser.getUserCode();
//                userName = loginUser.getUserName();
//            }
//
//            if (type == AutoCompleteType.All) {
//                for (String s : AUTO_COMPLETE_CREATOR) {
//                    if ("creator".equals(s)) {
//                        this.autoComplateField(cls, onlyArg, s, userCode);
//                    } else if ("creatorName".equals(s)) {
//                        this.autoComplateField(cls, onlyArg, s, userName);
//                    } else if ("createTime".equals(s)) {
//                        this.autoComplateField(cls, onlyArg, s, new Date());
//                    }
//                }
//            }
//            for (String s : AUTO_COMPLETE_UPDATER) {
//                if ("updater".equals(s)) {
//                    this.autoComplateField(cls, onlyArg, s, userCode);
//                } else if ("updaterName".equals(s)) {
//                    this.autoComplateField(cls, onlyArg, s, userName);
//                } else if ("updateTime".equals(s)) {
//                    this.autoComplateField(cls, onlyArg, s, new Date());
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void autoComplateField(Class<?> cls, Object source, String fieldName, Object value) {
//        try {
//            if (cls == null) {
//                return;
//            }
//            Field creator = cls.getDeclaredField(fieldName);
//
//            creator.setAccessible(true);
//            creator.set(source, value);
//            creator.setAccessible(false);
//        } catch (Exception e) {
//            // e.printStackTrace();
//            this.autoComplateField(cls.getSuperclass(), source, fieldName, value);
//        }
//    }
//
//
//}
