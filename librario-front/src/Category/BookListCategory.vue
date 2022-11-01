<template>
    <div class="">
    <h3>
        Kategoria: {{this.categoryData}}
    </h3>
    <el-input
            placeholder="Wyszukaj po tytule"
            v-model="inputText"
            clearable>
    </el-input>
    <el-table
            :data="filteredTableData"
            style="width: 100%"
            id="booksList"
            empty-text="Brak wyników"
    >
        <el-table-column prop="id" label="Identyfikator"></el-table-column>
        <el-table-column prop="name" label="Tytuł"></el-table-column>
        <el-table-column prop="author" label="Autor"></el-table-column>
        <el-table-column prop="length" label="Ilość stron"></el-table-column>
    </el-table>
</div>
    
</template>

<script>
    export default {
        name: "BookListCategory",
        data() {
            return {
                categoryData: this.$route.params.category,
                tableData: [],
                inputText: '',
            }
        },
        methods: {
            loadCategoryBooks: function () {
                this.$axios.get(this.$api + "/rest/books/list/" + this.categoryData, {})
                    .then(resp => {
                        this.tableData = resp.data;
                        console.log(this.tableData);
                    })
                    .catch(resp => {
                        console.log("error", resp);
                    })
            }
        },
        computed: {
            filteredTableData: function(){
                return this.tableData.filter((book) => {
                    if (book.name.toLowerCase().match(this.inputText.toLowerCase())) {
                        return book.name;
                    }
                });
            }
        },
        mounted() {
            this.loadCategoryBooks();
        }
    }
</script>

<style>

</style>