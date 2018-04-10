<template>
    <div>
        <el-dialog title="请登录" :visible="dialogFormVisible" width="30%">
            <el-form :model="loginUser" ref="loginForm" :rules="rules">
                <form-item :data="loginUser" :items="itemsMap"></form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="reset('loginForm')">重置</el-button>
                <el-button type="primary" @click="login('loginForm')">登录</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    /*eslint-disable*/
    import ElForm from "element-ui/packages/form/src/form";
    import FormItem from "@/components/base/FormItem";

    import {ajaxGet, ajaxPost} from "@/common/httpUtil";
    import {setCookie} from "@/common/commonUtil";


    export default {
        components: {ElForm, FormItem},
        name: "login",
        data() {
            return {
                loading: false,
                isSubmitting: false,
                dialogFormVisible: true,
                itemsMap: {
                    account: {
                        label: '账号',
                        placeholder:
                            '请输入账号',
                    },
                    password: {
                        type: 'password',
                        label: '密码',
                        placeholder:
                            '密码随便输',
                    },

                },
                loginUser: {
                    account: undefined,
                    password: undefined
                },
                rules: {
                    account: [{required: true, message: '请输入账号', trigger: 'blur'}],
                    password: [{required: true, message: '密码随便输', trigger: 'blur'}]
                }
            }
        },
        methods: {
            reset(formName) {
                this.$refs[formName].resetFields();
                this.isSubmitting = false;

            },
            login(formName) {
                var vm = this;
                if (vm.isSubmitting) {
                    vm.$message.warning('请勿重复提交!!!');
                    return false;
                }
                vm.isSubmitting = true;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        ajaxPost(apis._login, this.loginUser, (result) => {
                            if (result != null && result != "") {
                                setCookie(consts.token_name, result);
                                vm.$message({
                                    message: '登陆成功,页面跳转中!',
                                    duration: 1000,
                                    type: 'success',
                                    onClose: function (msg) {
                                        vm.$router.push(vm.$route.query.redirect || '/');
                                    }
                                });
                            } else {
                                vm.$message.error('账号或者密码错误!');
                                vm.isSubmitting = false;
                            }
                        }, (e) => {
                            vm.$message.error(e);
                            vm.isSubmitting = false;
                        });

                    } else {
                        console.log('error submit!!');
                        vm.isSubmitting = false;
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>