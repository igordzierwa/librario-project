<template>
    <el-row type="flex" class="row-bg" justify="space-around" >

        <el-col :span="18">
            <el-form ref="Login" :rules="rules" :model="model" v-loading="loading">
                <div class="notlogged" v-if="!userLogged">
                    <el-form-item label="Login:" prop="login">
                        <el-input v-model="model.login"></el-input>
                    </el-form-item>
                    <el-form-item label="Hasło:" prop="password">
                        <el-input type="password" v-model="model.password" max="9999"></el-input>
                    </el-form-item>
                    <el-button type="success" @click="handleSubmit">Zaloguj</el-button>
                    <div class ="register">
                        <el-button @click="routeToRegister">Zarejestruj się</el-button>
                    </div>
                </div>
                <div class="logged" v-else>
                    <h4>Jesteś zalogowany</h4>
                    <el-button type="success" @click="handleLogout">Wyloguj się</el-button>
                </div>
            </el-form>
        </el-col>

    </el-row>
</template>
<script>
    //komunikacja między login,availablebooks i userReservations
    export default {
        name: 'Login',
        data() {
            return {
                componentName: 'Login',
                mydata: [],
                loading: false,
                //wprowadzona zmienna potrzebna do rezerwacji
                userLoggedId: -1,
                userLogged: false,
                model: {
                    login: "",
                    password: "",
                },

                rules: {
                    login: [
                        {required: true, message: "Pole wymagane", trigger: 'change'}
                        //{ required: true, type: 'email', message: "Pole wymagane", trigger: 'change' }
                    ],
                    password: [
                        {required: true, message: "Pole wymagane", trigger: 'change'}
                    ],
                }
            }
        },
        methods: {
            checkIfLogged(){
                console.log("checking logged user")
                this.$axios.get(this.$api + "/rest/session")
                    .then(value => {
                        console.log("OK!", );
                        if(value.data[0] !== "ROLE_ANONYMOUS") {
                            this.$emit("loggedSuccessful", value);
                            this.userLogged = true;
                            this.$store.state.userRole = value.data[0];
                        }
                        else {
                            this.userLogged = false;
                            this.$emit("loggedUnSuccessful", error);
                        }
                    })
                    .catch(error => {
                        console.log("err", error);
                        this.$emit("loggedUnSuccessful", error);
                    })
            },
            handleLogout(){
                this.$axios.get(this.$api + "/logout")
                    .then(value => {
                        console.log("OK!", value);
                        this.$emit("loggedUnSuccessful", value);
                        this.userLogged = false;
                        localStorage.removeItem('loggedID');
                        this.model.login = "";
                        this.model.password = "";
                        this.$store.state.userRole = "";
                        this.$router.push("/");
                    })
                    .catch(error => {
                        console.log("err", error);

                    })
            },
            handleSubmit(){
                if(!this.valid(this.componentName))
                    return false;
                this.$axios({
                    method: 'POST',
                    url: this.$api + "/login",
                    withCredentials: true,
                    data:  JSON.stringify({
                        username:this.model.login,
                        password:this.model.password
                    }),
                    headers: {'Content-Type': 'raw'},

                }).then(resp => {
                        console.log(resp);
                        this.$message({
                            message: 'Poprawne dane logowania.',
                            type: 'success'
                        });
                        this.$emit("loggedSuccessful", resp);
                        this.userLogged = true;
                        this.loading = false;
                        this.$store.state.userRole = resp.data;
                        console.log(this.$store.state.userRole);

                        this.$axios.get(this.$api + "/rest/user/" + this.model.login)
                        .then(variable => {
                            this.userLoggedId = variable.data;
                            localStorage.setItem('loggedID', this.userLoggedId);
                        })
                    })
                    .catch(error => {
                        console.log("err", error);
                        this.$message({
                            message: 'Błędne dane logowania.',
                            type: 'error'
                        });
                        this.userLogged = false;
                        this.loading = false;
                    });
                console.log("eeee");
            },
            routeToRegister(){
                this.$router.push("/register/");
            },
            validate() {
                return new Promise((resolve, reject) => {
                    this.$refs.Application.validate((valid) => {
                        this.$emit('on-validate', valid, this.model)
                        resolve(valid);
                    });
                })
            },
        },
        mounted() {
            this.checkIfLogged();
        }

    }
</script>
<style lang="scss">
    .register{
        float: right;
    }
    .logged{
        text-align:center;
        margin:auto;
    }
</style>