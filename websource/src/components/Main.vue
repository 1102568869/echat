<template>
    <div id="bill">
        <el-container>
            <el-header>
                <div class="content-left">
                    <span style="display: block;height: 40px;width: 390px;text-align: center;line-height: 40px;font-size: 40px;">欢迎来到叮当家</span>
                    <!--<img src="./assets/washmore.png">-->
                </div>
                <div class="content-right">
                    <el-button type="primary" round plain :style="{background:'url(' + loginMember.image+ ') center'}">
                        {{loginMember.account}}
                    </el-button>
                    <el-button round @click="logOut">登出</el-button>
                </div>
            </el-header>

            <el-container>
                <el-aside width="200px">
                    <el-menu :default-openeds="['1']">
                        <el-submenu index="1">
                            <template slot="title"><i class="el-icon-refresh"></i>成员列表</template>
                            <el-menu-item v-for="user in userlist" :index="user">{{user}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-container>
                    <el-main>
                        <div  v-for='msg in msglist'>
                            <div :class="msg.account===loginMember.account?'content-right':'content-left'">
                                {{msg.account===loginMember.account?'':msg.account+':'}}
                                <span v-html="formatContent(msg.content)"></span>
                            </div>
                            <div style="clear: both"></div>
                        </div>
                    </el-main>
                    <el-footer>
                        <div class="input">
                            <el-input
                                    type="textarea"
                                    :rows="4"
                                    placeholder="请输入内容"
                                    v-model="message" @keyup.enter.alt.native="sendMsg">
                            </el-input>
                        </div>
                        <el-row class="content-right">
                            <el-button size="mini" type="danger" icon="el-icon-delete" circle></el-button>
                            <el-button size="mini" type="success" icon="el-icon-check" circle
                                       @click="sendMsg"></el-button>
                        </el-row>
                    </el-footer>
                </el-container>
            </el-container>
        </el-container>
    </div>
</template>
<script>
    import {ajaxGet, ajaxPost} from "@/common/httpUtil";
    import {getCookie, removeCookie} from "@/common/commonUtil";
    /*eslint-disable*/
    export default {
        name: 'welcome',
        data() {
            return {
                webSocket: Object,
                message: '',
                msglist: [],
                userlist: [],
                loginMember: {
                    account: '',
                    image: '',
                },
                sendMsgData: {
                    type: Number,
                    account: String,
                    content: String,
                    ext: Object
                },
                recMsgData: {
                    type: Number,
                    account: String,
                    content: String,
                    ext: Object
                }
            }
        },
        methods: {
            formatContent(comment) {
                return comment != null ? comment.replace(/\n/g, '<br/>') : '';
            },
            sendMsg() {
                this.sendMsgData.content = this.message;
                this.sendMsgData.account = this.loginMember.account;
                this.sendMsgData.type = 100;
                this.webSocket.send(JSON.stringify(this.sendMsgData));
                this.message = '';
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
            },
            onMessage(event) {
                console.info("onMessage", event.data);
                this.recMsgData = JSON.parse(event.data);
                if (this.recMsgData.type == 101) {
                    this.msglist.push({
                        account: this.recMsgData.account,
                        content: this.recMsgData.content
                    })
                } else if (this.recMsgData.type == 91) {
                    this.userlist = JSON.parse(this.recMsgData.content);
                    this.loginMember.account = this.recMsgData.account;
                } else if (this.recMsgData.type == 92) {
                    this.userlist = JSON.parse(this.recMsgData.content);
                }
            }
        },
        mounted() {

        },
        created() {
            ajaxGet(apis._verifyToken, (data) => {
                if (data) {
                    this.loginMember.account = data;
                } else {
                    removeCookie(consts.token_name);
                    this.$router.push({name: 'login'});
                }
            });

            var vm = this;

            this.webSocket = new WebSocket('ws://ws.washmore.tech/webSocket');
            this.webSocket.onerror = function (event) {
                console.error("onError", event);
            };
            this.webSocket.onopen = function (event) {
                console.info("onOpen", event);
                vm.sendMsgData.content = getCookie(consts.token_name);
                vm.sendMsgData.account = vm.loginMember.account;
                vm.sendMsgData.type = 90;
                vm.webSocket.send(JSON.stringify(vm.sendMsgData));
            };
            this.webSocket.onmessage = function (event) {
                vm.onMessage(event);
            };

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
        line-height: 100px;
        position: fixed;
        margin-top: 60px;
        z-index: 98;
        width: 100px;
        left: 0px;
    }

    .el-main {
        background-color: #E9EEF3;
        color: #333;
        display: block;
        min-height: 400px;
        margin-top: 60px;
        margin-left: 200px;
    }

    .el-footer {
        background-color: #08ffe9;
        color: #333;
        display: block;
        margin-top: 20px;
        min-height: 140px;
        margin-left: 200px;
    }

    .el-footer .input {
        padding: 5px 0;

    }

    body > .el-container {
        margin-bottom: 40px;
    }

    .content-right {
        float: right;
    }

    .content-left {
        float: left;
        margin-top: 10px;
    }

</style>
