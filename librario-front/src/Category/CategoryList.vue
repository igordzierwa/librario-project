<template>
    <div class="">
        <h3>
            Lista wszystkich kategorii
        </h3>
        <el-table
                :data="categoryData"
                style="width: 100%"
                id="categoryList"
                empty-text="Brak wyników"
        >
            <el-table-column prop ="category" label="Kategoria"></el-table-column>
            <el-table-column prop ="count" label="Ilość książek"></el-table-column>
            <el-table-column
                    fixed="right"
                    label="Akcje"
                    width="120">
                <template slot-scope="scope">
                    <router-link tag="el-button" class="myClass" id="button" :to="'/books/list/' + scope.row.category">Pokaż</router-link>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
    export default {
        name: "CategoryList",
        data() {
            return {
                categoryData: [],
            }
        },
        methods: {
            loadCategory: function () {
                this.$axios.get(this.$api + "/rest/categories", {})
                    .then(resp => {
                        this.categoryData = resp.data;
                        console.log(this.categoryData);
                    })
                    .catch(resp => {
                        console.log("error", resp);
                    })
            },
        },
        mounted() {
            this.loadCategory()
        }
    }
</script>

<style>
</style>