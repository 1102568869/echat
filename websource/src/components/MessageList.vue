<template>
    <div id="bill">

    </div>
</template>
<script>
    import {ajaxGet, ajaxPost} from "@/common/httpUtil";
    import {removeCookie} from "@/common/commonUtil";
    /*eslint-disable*/
    export default {
        name: 'welcome',
        data() {
            return {
                loginMember: {
                    name: '',
                    image: ''
                }
            }
        },
        methods: {
            chooseToShow() {
                this.$prompt('请输入需要查看的用户名', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /^.+$/,
                    inputErrorMessage: '用户名不能为空'
                }).then(({value}) => {
                    this.$router.push({name: 'RouterDemoOne', params: {'userName': value}});
                }).catch((e) => {
                    console.error(e)
                    this.$message({
                        type: 'info',
                        message: '取消输入'
                    });
                });
            },
            logOut() {
                this.$confirm('确认退出?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    removeCookie(consts.token_name);
                    this.$router.push({name: 'login'});
                }).catch(() => {
                });
            }
        },
        created() {
            ajaxGet(apis._verifyToken, (data) => {
                if (data && data.name) {
                    this.loginMember = data;
                }
            });
        }
    }
</script>
<style scoped>
    .el-header {
        top: 0px;
        left: 0px;
        z-index: 99;
        width: 100%;
        position: fixed;
    }

    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        height: 60px;
        line-height: 60px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
        position: fixed;
        margin-top: 60px;
        z-index: 98;
        left: 0px;
    }

    .el-main {
        background-color: #E9EEF3;
        color: #333;
        display: block;
        margin-top: 60px;
        margin-left: 200px;
    }

    body > .el-container {
        margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
        line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
        line-height: 320px;
    }

    .header-right {
        float: right;
    }

    .header-left {
        float: left;
        margin-top: 10px;
    }

</style>
