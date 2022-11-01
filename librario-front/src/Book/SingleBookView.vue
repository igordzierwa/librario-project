<template>
    <div id="bookInfo">
        <el-container>
            <el-header><b>Tytuł książki:</b></el-header>
            <el-main>{{this.tableData.name}}</el-main>
            <el-header><b>Autor:</b></el-header>
            <el-main>
                <router-link :to="'/singleauthor/' + this.tableData.author">
                    <el-link>{{this.tableData.author}}<i class="el-icon-view el-icon--right"></i></el-link>
                </router-link>
            </el-main>
            <el-header><b>Kategoria:</b></el-header>
            <el-main>
                <router-link :to="'/books/list/' + this.tableData.category">
                    <el-link>{{this.tableData.category}}<i class="el-icon-view el-icon--right"></i></el-link>
                </router-link>
            </el-main>
            <el-header><b>Długość:</b></el-header>
            <el-main>{{this.tableData.length}}</el-main>
            <el-header><b>Opis:</b></el-header>
            <el-main>{{this.tableData.description}}</el-main>
            <el-header><b>Okładka:</b></el-header>
            <el-main>
                <img v-if="tableData.cover_photo && tableData.cover_photo.url" :src="'http:/localhost:8081/resources/gfx/'+tableData.cover_photo.url"/>
            </el-main>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "SingleBookView",

        data() {
            return {
                bookID: this.$route.params.id,
                tableData: [],
            }
        },
        methods: {
            loadBook: function () {
                this.$axios.get(this.$api + "/rest/books/" + this.bookID, {})
                .then(resp => {
                    this.tableData = resp.data;
                    console.log(this.tableData)
                }).catch(resp => {
                    console.log("error", resp);
                })
            }
        },
        mounted() {
            this.loadBook();
        }
    }
</script>

<style lang="scss">
    #bookInfo{
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