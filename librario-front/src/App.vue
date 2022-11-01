<template>
    <el-form ref="Application">
        <main-menu></main-menu>
        <router-view :key="$route.fullPath"></router-view>
        <footer_component></footer_component>
    </el-form>
</template>

<script>
    import MainMenu from "./Menu.vue";
    import Footer_component from "./Footer.vue";
    export default {
        components: {Footer_component, MainMenu},
        name: 'app',
        data () {
            return{
                name: 'test',
                mydata: []
            }
        },
        methods: {

        },
        mounted() {
            console.log("LOG", this.mydata);
        }
    }
</script>

<style lang="scss">
    @import url('https://fonts.googleapis.com/css?family=Roboto:300,400,700&display=swap');
    body{
        font-family: 'Roboto', sans-serif;
        font-weight: 400;
    }
</style>

