package com.zhongan.icare.account.dao.generate;

import org.mybatis.generator.exception.XMLParserException;

public class MybatisGenerator
{

    public static void main(String[] args) throws Exception, XMLParserException
    {

        /////-----------------------暂时不用-satrt------------------------------------////
//        List<String> warnings = new ArrayList<String>();
//        boolean overwrite = true;
//        ConfigurationParser cp = new ConfigurationParser(warnings);
//        Configuration config = cp.parseConfiguration(MybatisGenerator.class.getClassLoader().getResourceAsStream(
//                MybatisGenerator.class.getPackage().getName().replaceAll("\\.", "/") + "/" + "cfg.xml"));
//        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//        myBatisGenerator.generate(null);
//        Context ctx = config.getContext("icare");
//
//        JavaClientGeneratorConfiguration jcgcfg = ctx.getJavaClientGeneratorConfiguration();
//
//        String pkg = jcgcfg.getTargetPackage();
//        String prjPath = jcgcfg.getTargetProject();
//
//        File doDir = new File(prjPath, pkg.replaceAll("\\.", "/"));
//
//        SqlMapGeneratorConfiguration sqlcfg = ctx.getSqlMapGeneratorConfiguration();
//
//        String sqlProjectPath = sqlcfg.getTargetProject();
//        String sqlPkg = sqlcfg.getTargetPackage();
//
//        File sqlMapDir = new File(sqlProjectPath, sqlPkg.replaceAll("\\.", "/"));
//
//        for (File doFile : doDir.listFiles(new FileFilter() {
//            public boolean accept(File pathname) {
//                return pathname.getName().endsWith("DOMapper.java");
//            }
//        })) {
//            String doName = doFile.getName();
//            String clzName = doName.substring(0, doName.indexOf(".java"));
//            String clzQName = pkg + "." + clzName;
//            String filanName = doName.substring(0, doName.indexOf("DOMapper.java")) + "DAO";
//            String filanClzQName = pkg + "." + filanName;
//            String content = FileUtils.readFileToString(doFile, "UTF-8");
//            String finalContent = content.replaceAll(clzName, filanName);
//            finalContent = finalContent.replaceAll("extends ICommonDAO",
//                    "extends ICommonDAO<" + doName.substring(0, doName.indexOf("DOMapper.java")) + "DO>");
//            FileUtils.write(new File(doFile.getParent(), filanName + ".java"), finalContent, false);
//            FileUtils.deleteQuietly(doFile);
//
//            String srcSqlFileName = doName.substring(0, doName.indexOf("DOMapper.java")) + "DOMapper.xml";
//            String destSqlFileName = doName.substring(0, doName.indexOf("DOMapper.java")) + "Mapper.xml";
//            File sqlFile = new File(sqlMapDir, srcSqlFileName);
//            String sqlContent = FileUtils.readFileToString(sqlFile, "UTF-8");
//            finalContent = sqlContent.replaceAll(clzQName, filanClzQName);
//            FileUtils.write(new File(sqlFile.getParent(), destSqlFileName), finalContent, false);
//            FileUtils.deleteQuietly(sqlFile);
//
//        }
        /////-----------------------暂时不用-end------------------------------------////

        //        for (File doFile : sqlMapDir.listFiles(new FileFilter() {
        //            public boolean accept(File pathname) {
        //                return pathname.getName().endsWith("DOMapper.xml");
        //            }
        //        })) {
        //            String doName = doFile.getName();
        //            String clzName = doName.substring(0, doName.indexOf("DOMapper.xml"));
        //            String filanName = doName.substring(0, doName.indexOf("DOMapper.xml")) + "Mapper.xml";
        //            String content = FileUtils.readFileToString(doFile, "UTF-8");
        //            String finalContent = content.replaceAll(clzName, filanName);
        //            finalContent = finalContent.replaceAll("extends ICommonDAO",
        //                    "extends ICommonDAO<" + doName.substring(0, doName.indexOf("DOMapper.java")) + "DO>");
        //            FileUtils.write(new File(doFile.getParent(), filanName + ".java"), finalContent, false);
        //            FileUtils.deleteQuietly(doFile);
        //        }
    }
}
