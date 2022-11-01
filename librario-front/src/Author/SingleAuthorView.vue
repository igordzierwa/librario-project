<template>
    <div id="authorInfo">
        <el-container>
            <el-header><b>Imię i Nazwisko:</b></el-header>
            <el-main>{{this.tableData.name}}</el-main>
            <el-header><b>Rok urodzenia:</b></el-header>
            <el-main>{{this.tableData.year}}</el-main>
            <el-header><b>Opis:</b></el-header>
            <el-main>{{this.tableData.description}}</el-main>
            <el-header><b>Liczba książek autora:</b></el-header>
            <el-main>
                <p>{{this.numberOfBooks}}</p>
                    <router-link :to="'/singleauthor/books/' + this.tableData.name">
                        <el-link>Zobacz książki autora<i class="el-icon-view el-icon--right"></i></el-link>
                    </router-link>
            </el-main>
            <el-header><b>Okładka:</b></el-header>
            <el-main>
                <div class="image-profile">
                    <img v-if="tableData.profile_photo && tableData.profile_photo.url" :src="'http:/localhost:8081/resources/gfx/'+tableData.profile_photo.url"/>
                </div>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "SingleAuthorView",

        data() {
            return {
                authorName: this.$route.params.name,
                tableData: [],
                numberOfBooks: 0,
            }
        },
        methods: {
            loadAuthor: function () {
                this.$axios.get(this.$api + "/rest/author/name/" + this.authorName, {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData)
                    }).catch(resp => {
                    console.log("error", resp);
                })
            },
            loadNumberOfBooks: function () {
                this.$axios.get(this.$api + "/rest/books/count/author/" + this.authorName, {})
                    .then(resp => {
                        this.numberOfBooks = resp.data;
                        console.log(this.numberOfBooks)
                    }).catch(resp => {
                        console.log("error", resp);
                })
            }
        },
        mounted() {
            this.loadAuthor();
            this.loadNumberOfBooks();
        }
    }
</script>

<style>
    #authorInfo{
        text-align:center;
        margin:auto;
    }
    .el-header {
        background-color: #e0e4e9;
        color: #333;
        text-align: center;
        line-height: 60px;
    }
    img {
        max-width: 400px;
        align-content: center;
    }
</style>