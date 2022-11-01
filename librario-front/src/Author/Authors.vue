<template>
    <div id="authors">
        <h3 v-if="editingAuthorId">
            Edytuj autora o numerze: {{editingAuthorId}}
        </h3>
        <h3 v-else>
            Dodaj Autora
        </h3>

<el-form>
    <el-row>
        <el-form ref="AddAuthor" :rules="rules" :model="model" v-loading="loading">
            <el-form-item label="Imię i nazwisko:" prop="name">
                <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Rok urodzenia:" prop="year">
                <el-input type="number" v-model="model.year" max="9999"></el-input>
            </el-form-item>
            <el-form-item label="Opis:" prop="description">
                <el-input type="textarea" v-model="model.description"></el-input>
            </el-form-item>
            <el-form-item label="Zdjęcie">
                <el-upload
                        class="avatar-uploader"
                        action="http://localhost:8081/rest/picture"
                        :show-file-list="false"
                        :on-success="handlePhotoUploadSuccess"
                        :on-error="handlePhotoUploadError">
                        <img v-if="model.imageUrl" :src="model.imageUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-button type="success" @click="handleSubmit">Zapisz!</el-button>
        </el-form>
    </el-row>
</el-form>
    </div>
</template>
<script>
export default {
    name: 'authors',
    props: ['edit'],
    data(){
        const validateYear = (r, value, callback)  => {
            if(!/(\+?)[0-9](\-?)[0-9](\ ?)[0-9]/g.test(value) || value.length < 4 || value.length > 4){
                callback(new Error('Contains letter or too short or loo long'));
            } else {
                callback();
            }
        };

        return {
            componentName: 'AddAuthor',
            mydata: [],
            loading: false,
            editingAuthorId: false,
            model:{
                name: "",
                year: "",
                description: "",
                imageUrl: "",
                filename: "",
                picture_name: "",
            },
            rules:{
                name: [
                    { required: true, message: "Pole wymagane", trigger: 'change' }
                    //{ required: true, type: 'email', message: "Pole wymagane", trigger: 'change' }
                ],
                year: [
                    { required: true, max: 9999, validator: validateYear,  message: "Wpisz poprawny rok urodzenia", trigger: 'change' }
                ],
                description: [
                    { required: true,  message: "Pole wymagane", trigger: 'change' }
                ],
            }
        }
    },
    methods: {
        handlePhotoUploadSuccess: function(data, file){
            console.log(data);
            this.model.imageUrl = URL.createObjectURL(file.raw);
            this.model.picture_name = "Author Picture";
            this.model.filename = data.name;
        },
        handlePhotoUploadError: function(error, vv){
          this.$message({message: error, type: "error"})
        },
        handleSubmit: function(){
            console.log(this.$refs);
            if(!this.valid(this.componentName))
                return false;

            this.loading = true;
            this.model.year = parseInt(this.model.year);
            var formData = this.prepareFormDataFromModel(this.model);
            var apiEndpoint = this.$api + "/rest/author";
            var method = "POST";

            if(this.editingAuthorId) {
                apiEndpoint = apiEndpoint + "/" + this.editingAuthorId
                method = "PUT";
            }
            this.$axios({
                method: method,
                url: apiEndpoint,
                data: formData,
                options: {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
                }).then(resp => {
                    console.log(resp)
                    this.loading = false;
                    if(method == "PUT"){
                        this.$message({
                            message: 'Poprawnie zmodyfikowano autora.',
                            type: 'success'
                        });
                        return;
                    }
                    this.$message({
                        message: 'Poprawnie dodano autora.',
                        type: 'success'
                    });
                })
                .catch(error => {
                    console.log(error)
                    this.loading = false;
                    this.$message({
                        message: 'Error przy modyfikacji autora.',
                        type: 'error'
                    });
                })
        },
        loadAuthor: function(id){
            this.$axios
                .get(this.$api + "/rest/author/" + id,
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(resp => {
                    console.log(resp);
                    console.log(resp.data, this.model);
                    this.model.name = resp.data.name;
                    this.model.year = resp.data.year;
                    this.model.filename = "";
                    this.model.description = resp.data.description;
                    this.model.imageUrl = this.$api + "/resources/gfx/"+resp.data.profile_photo.url;

                    this.loading = false;
                })
                .catch(error => {
                    console.log(error)
                    this.loading = false;
                })
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
    mounted: function() {
        console.log("PROP", this.edit);

        if(this.edit) {  // property przekazane w VueRouter - patrz main.js
            this.editingAuthorId = this.$route.params.id;
            this.loadAuthor(this.editingAuthorId);

        }
    },


    // watch: {
    //     $route(to, from) {
    //
    //     }
    // }

}
</script>
<style lang="scss">
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

</style>