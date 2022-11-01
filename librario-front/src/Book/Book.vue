<template>
    <div id="authors">
        <h3 v-if="editingId">
            Edytuj książkę o numerze: {{editingId}}
        </h3>
        <h3 v-else>
            Dodaj Książkę
        </h3>

<el-form>
    <el-row>
        <el-form ref="AddBook" :rules="rules" :model="model" v-loading="loading">
            <el-form-item label="Nazwa książki" prop="name">
                <el-input v-model="model.name"></el-input>
            </el-form-item>
            <el-form-item label="Długość:" prop="length">
                <el-input type="text" v-model="model.length" ></el-input>
            </el-form-item>
            <el-form-item label="Stan magazynowy:" prop="stockCount">
                <el-input type="text" v-model="model.stockCount" ></el-input>
            </el-form-item>
            <el-form-item label="Opis:" prop="description">
                <el-input type="textarea" v-model="model.description"></el-input>
            </el-form-item>
            <el-form-item label="Autor:" prop="author">
                <el-select v-loading="authorsSelectLoading" v-model="model.author" placeholder="Zacznij wpisywać imię autora" :no-match-text="'Brak wyników'" filterable>
                    <el-option
                            v-for="item in authors"
                            :key="item.id"
                            :label="item.name"
                            :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="Kategoria książki:" prop="category">
                <el-select v-loading="categorySelectLoading" v-model="model.category" placeholder="Zacznij wpisywać nazwę kategorii" :no-match-text="'Brak wyników'" filterable>
                    <el-option
                            v-for="item in categories"
                            :key="item.category"
                            :label="item.category"
                            :value="item.category">
                    </el-option>
                </el-select>
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
        const validateNumber = (r, value, callback)  => {
            console.log(r, value, callback);
            if(/^[0-9]*$/gm.test(value) && value.toString().length > 0){
                callback();
            } else {
                callback(new Error('Contains letter or too short or loo long'));
            }
        };

        return {
            componentName: 'AddBook',
            mydata: [],
            loading: false,
            authorsSelectLoading: false,
            categorySelectLoading: false,
            editingId: false,
            authors: [],
            categories: [],
            model:{
                name: "",
                length: "",
                author: "",
                stockCount: "",
                description: "",
                category: "",
                imageUrl: "",
                filename: "",
                picture_name: "",
            },
            rules:{
                name: [
                    { required: true, message: "Pole wymagane", trigger: 'change' }
                    //{ required: true, type: 'email', message: "Pole wymagane", trigger: 'change' }
                ],
                length: [
                    { required: true, validator: validateNumber,  message: "Wpisz poprawna dlugosc", trigger: 'change' }
                ],
                stockCount: [
                    { required: true, validator: validateNumber,  message: "Wpisz poprawny stan magazynowy", trigger: 'change' }
                ],
                author: [
                    { required: true,  message: "Pole wymagane", trigger: 'change' }
                ],
                category: [
                    { required: true,  message: "Pole wymagane", trigger: 'change' }
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
            this.model.stockCount = parseInt(this.model.stockCount);
            var formData = this.prepareFormDataFromModel(this.model);
            var apiEndpoint = this.$api + "/rest/books";
            var method = "POST";

            if(this.editingId) {
                apiEndpoint = apiEndpoint + "/" + this.editingId
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
                            message: 'Poprawnie zmodyfikowano książke.',
                            type: 'success'
                        });
                        return;
                    }
                    this.$message({
                        message: 'Poprawnie dodano książkę.',
                        type: 'success'
                    });
                })
                .catch(error => {
                    console.log(error)
                    this.loading = false;
                    this.$message({
                        message: 'Error przy modyfikacji książki.',
                        type: 'error'
                    });
                })
        },
        loadAuthors: function(){
            this.authorsSelectLoading = true;
            this.$axios
                .get(this.$api + "/rest/author/",
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(resp => {
                    console.log(resp);
                    console.log(resp.data, this.model);
                    this.authors = resp.data;
                    this.authorsSelectLoading = false;
                })
                .catch(error => {
                    console.log(error);
                    this.authorsSelectLoading = false;
                })
        },
        loadCategories: function(){
            this.authorsSelectLoading = true;
            this.$axios
                .get(this.$api + "/rest/categories/",
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(resp => {
                    console.log(resp);
                    console.log(resp.data, this.model);
                    this.categories = resp.data;
                    this.categorySelectLoading = false;
                })
                .catch(error => {
                    console.log(error);
                    this.categorySelectLoading = false;
                })
        },
        loadEntity: function(id){
            this.$axios
                .get(this.$api + "/rest/books/" + id,
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(resp => {
                    console.log(resp);
                    console.log(resp.data, this.model);
                    this.model.name = resp.data.name;
                    this.model.length = resp.data.length;
                    this.model.filename = "";
                    this.model.author = parseInt(resp.data.author);
                    this.model.stockCount = resp.data.stockCount;
                    this.model.category = resp.data.category;
                    this.model.imageUrl = this.$api + "/resources/gfx/"+resp.data.cover_photo.url;
                    this.model.description = resp.data.description;
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
            this.editingId = this.$route.params.id;
            this.loadEntity(this.editingId);
        }

        this.loadAuthors();
        this.loadCategories();

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