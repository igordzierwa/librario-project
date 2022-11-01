<template>
    <div id="addEmployee">
        <h3 v-if="edit===false">Dodaj pracownika</h3>
        <h3 v-else>Edytuj dane pracownika</h3>
        <el-form>
            <el-row>
                <el-form ref="model" :rules="rules" :model="model" v-loading="loading">
                    <el-form-item label="Nazwa użytkownika" prop="username">
                        <el-input v-model="model.username" :disabled=this.edit></el-input>
                    </el-form-item>
                    <el-form-item label="Hasło" prop="password" v-if=!this.edit>
                        <el-input v-model="model.password" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="Potwierdź hasło" prop="confirmPassword" v-if=!this.edit>
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
                    <div id="button"><el-button type="success" @click="handleSubmit('model')" v-if="edit===false">Dodaj</el-button>
                        <el-button type="success" @click="handleEdit" v-else>Zapisz</el-button></div>
                </el-form>
            </el-row>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "AddEmployee",
        props: ['edit'],
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
                mydata: [],
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
                loading: false,
                model:{
                    username: "",
                    password: "",
                    confirmPassword: "",
                    name:"",
                    surname:"",
                    email:"",
                }
            }
        },
        methods: {
            sendData: function(){
                this.loading = true;
                this.$axios
                    .post("http://localhost:8081/admin/employees/",
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
                        this.loading = false;
                        this.$message({
                            message: 'Pracownik dodany.',
                            type: 'success'
                        });
                    })
                    .catch(error => {
                        console.log(error);
                        this.loading = false;
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
                        });
                    })
            },
            loadEmployee: function(username){
                console.log(username);
                this.$axios
                    .get("http://localhost:8081/admin/employees/" + username,
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        console.log(resp);
                        console.log(resp.data, this.model);
                        this.model.username = resp.data.username;
                        this.model.name = resp.data.name;
                        this.model.surname = resp.data.surname;
                        this.model.email = resp.data.email;
                        this.loading = false;
                    })
                    .catch(error => {
                        console.log(error);
                        this.loading = false;
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
                        });
                    })
            },
            handleEdit: function(){this.loading = true;
                this.$axios
                    .put("http://localhost:8081/admin/employees/",
                        {
                            username:this.model.username,
                            name:this.model.name,
                            surname:this.model.surname,
                            email:this.model.email
                        },
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        console.log(resp);
                        this.loading = false;
                        this.$message({
                            message: 'Dane zaktualizowane',
                            type: 'success'
                        });
                    })
                    .catch(error => {
                        console.log(error);
                        this.loading = false;
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
                        });
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
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
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
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
                        });
                    })
            },
            validEmail: function (email) {
                var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            },
            handleSubmit: function (model) {
                this.$refs[model].validate((valid)=>{
                    if(valid){
                        this.sendData();
                    } else {
                        this.$message({
                            message:"Niepoprawne dane.",
                            type:"error"
                        })
                    }
                });
            },
        },
        mounted: function() {
            //console.log("PROP", this.edit);

            if(this.edit) {  // property przekazane w VueRouter - patrz main.js
                this.loadEmployee(this.$route.params.username);

            }
        },

    }
</script>

<style scoped lang="scss">
#addEmployee{
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