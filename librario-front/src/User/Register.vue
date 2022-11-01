<template>
    <div id="register">
        <h3>Rejestracja</h3>
        <el-form>
            <el-row>
                <el-form ref="model" :rules="rules" :model="model">
                    <el-form-item label="Nazwa użytkownika" prop="username">
                        <el-input v-model="model.username"></el-input>
                    </el-form-item>
                    <el-form-item label="Hasło" prop="password">
                        <el-input v-model="model.password" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="Potwierdź hasło" prop="confirmPassword">
                        <el-input v-model="model.confirmPassword" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="Imię" prop="name">
                        <el-input v-model="model.name" type="text"></el-input>
                    </el-form-item>
                    <el-form-item label="Nazwisko" prop="surname">
                        <el-input v-model="model.surname" type="text"></el-input>
                    </el-form-item>
                    <el-form-item label="E-Mail" prop="email">
                        <el-input v-model="model.email"></el-input>
                    </el-form-item>
                    <div id="button"><el-button type="success" @click="handleSubmit('model')">Zarejestruj</el-button>
                    </div>
                </el-form>
            </el-row>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Register",
        data(){
            let validateLogin = (rule, value, callback) => {
                if(value === ''){
                    callback(new Error("Pole wymagane"));
                } else {
                    this.checkUsernameAvailable(() => {
                        if(this.usernameAvailable){
                            callback();
                        } else {
                            callback(new Error("Nazwa użytkownika zajęta"));
                        }
                    });
                }
            };
            let validateEmail = (rule, value, callback) => {
              if(value === ''){
                  callback(new Error("Pole wymagane"));
              } else {
                  if(this.validEmail(this.model.email)) {
                      this.checkEmailAvailable(() => {
                          if (this.emailAvailable) {
                              callback();
                          } else {
                              callback(new Error("Email jest już w systemie"));
                          }
                      })
                  } else {
                      callback(new Error("Podaj e-mail w poprawnym formacie"));
                  }
              }
            };
            let validatePasswords = (rule, value, callback) => {
              if(value === ''){
                  callback(new Error("Pole wymagane"));
              } else {
                  if(this.model.password !== this.model.confirmPassword){
                      callback(new Error("Hasła się nie zgadzają!"));
                  } else {
                      callback();
                  }
              }
            };
            return {
                myData: [],
                usernameAvailable:false,
                emailAvailable:false,
                validated:false,
                model:{
                    username: "",
                    password: "",
                    confirmPassword: "",
                    name:"",
                    surname:"",
                    email:"",
                },
                rules: {
                    username: [
                        {required: true, validator:validateLogin, trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: "Pole wymagane", trigger: 'blur'}
                    ],
                    confirmPassword: [
                        {required: true, validator:validatePasswords, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: "Pole wymagane", trigger:'blur'}
                    ],
                    surname: [
                        {required: true, message: "Pole wymagane", trigger:'blur'}
                    ],
                    email: [
                        {required:true, validator:validateEmail, trigger:'blur'}
                    ],
                },
            }
        },
        methods: {
            sendData: function(){
                //this.loading = true;
                this.$axios
                    .post(this.$api + "/register",
                        {
                            username:this.model.username,
                            password:this.model.password,
                            confirmPassword:this.model.confirmPassword,
                            name:this.model.name,
                            surname:this.model.surname,
                            email:this.model.email
                        },
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        console.log(resp);
                        //this.loading = false;
                        this.$message({
                            message: 'Rejestracja przebiegła pomyślnie.',
                            type: 'success'
                        });
                        this.$router.push("/");
                    })
                    .catch(error => {
                        console.log(error);
                        //this.loading = false;
                        this.$message({
                            message: 'Błąd komunikacji z serwerem!',
                            type: 'error'
                        });
                    })
            },
            checkUsernameAvailable: function (method) {
                this.$axios
                    .post(this.$api + "/check/username",
                        {
                            username:this.model.username,
                        },
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        this.usernameAvailable = (resp.data === "true");
                        method();
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            checkEmailAvailable: function (method) {
                this.$axios
                    .post(this.$api + "/check/email",
                        {
                            email:this.model.email,
                        },
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        this.emailAvailable = (resp.data === "true");
                        method();
                    })
                    .catch(error => {
                        console.log(error);
                    })
            },
            handleSubmit: function (model) {
                this.$refs[model].validate((valid)=>{
                    if(valid){
                        this.sendData();
                    } else {
                        this.$message({
                            message:"Niepoprawne dane",
                            type:"error"
                        })
                    }
                });
            },
            validEmail: function (email) {
                var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
        },
        mounted: function() {
        },
        computed: {
        }

    }
</script>

<style scoped lang="scss">
    #register{
        h1{
            font-size: 14px;
        }
        h3{
            text-align:center;
            margin:auto;
        }
    }
    #button {
        text-align: center;
        margin: auto;
    }
</style>