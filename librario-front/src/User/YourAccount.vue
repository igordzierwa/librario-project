<template>
    <div id="youracc">
        <h3>Twoje dane</h3>
        <el-form>
            <el-row>
                <el-form ref="model" :rules="rules" :model="model">
                    <el-form-item label="Nazwa użytkownika" prop="username">
                        <el-input v-model="model.username" :disabled=true></el-input>
                    </el-form-item>
                    <el-form-item label="Imię" prop="name">
                        <el-input v-model="model.name" type="text" :disabled=true></el-input>
                    </el-form-item>
                    <el-form-item label="Nazwisko" prop="surname">
                        <el-input v-model="model.surname" type="text" :disabled=true></el-input>
                    </el-form-item>
                    <el-form-item label="E-Mail" prop="email">
                        <el-input v-model="model.email" :disabled=true></el-input>
                    </el-form-item>
                    <div id="button">
                    </div>
                </el-form>
            </el-row>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "YourAccount",
        data(){
            return {
                loggedUserId: "",
                model:{
                    username: "",
                    name:"",
                    surname:"",
                    email:"",
                },
                rules: {
                },
            }
        },
        methods: {
            getUserById: function () {
                this.$axios
                    .get("http://localhost:8081/user/" + this.loggedUserId,
                        {headers: {'Content-Type': 'application/json'}})
                    .then(resp => {
                        console.log(resp);
                        console.log(resp.data, this.model);
                        this.model.username = resp.data.username;
                        this.model.name = resp.data.name;
                        this.model.surname = resp.data.surname;
                        this.model.email = resp.data.email;
                    })
                    .catch(error => {
                        console.log(error);
                        this.loading = false;
                        this.$message({
                            message: 'Błąd komunikacji z serwerem',
                            type: 'Error'
                        });
                    })
            }
        },
        mounted: function() {
            this.loggedUserId = localStorage.getItem('loggedID');
            this.getUserById();
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