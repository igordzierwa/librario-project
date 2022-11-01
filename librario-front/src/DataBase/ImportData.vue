<template>
    <el-row type="flex" class="row-bg" justify="space-around">

        <el-col :span="6">
            <div class=" 404error grid-content bg-purple-light">
                <br><br>

                    Tu będzie importowanie....<br>
                    można wykorzystać taki progressbar
                    <el-progress :text-inside="true" :stroke-width="26" :percentage="70"></el-progress>
                    <el-progress :text-inside="true" :stroke-width="24" :percentage="100" status="success"></el-progress>
                    <el-progress :text-inside="true" :stroke-width="22" :percentage="80" status="warning"></el-progress>
                    <el-progress :text-inside="true" :stroke-width="20" :percentage="50" status="exception"></el-progress>

                <br>
                <el-button onclick="window.location.href = '/'">Wróć do strony głównej</el-button>

            </div>
        </el-col>

    </el-row>
</template>
<script>
    export default {
        name: 'ImportData'
    }
</script>
<style lang="scss">
    .404error{
        text-align: center;
    }
</style>