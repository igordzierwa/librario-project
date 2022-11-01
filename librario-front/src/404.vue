<template>
    <el-row type="flex" class="row-bg" justify="space-around">

        <el-col :span="6">
            <div class=" 404error grid-content bg-purple-light">
                <br><br>
                <el-alert type="error"
                          :closable="false">
                    Błąd 404
                </el-alert>
                <br>
                <el-button onclick="window.location.href = '/'">Wróć do strony głównej</el-button>

            </div>
        </el-col>

    </el-row>
</template>
<script>
    export default {
        name: 'notfound_component'
    }
</script>
<style lang="scss">
    .404error{
        text-align: center;
    }
</style>