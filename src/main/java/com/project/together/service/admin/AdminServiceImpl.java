package com.project.together.service.admin;

import com.project.together.domain.TableData;
import com.project.together.mapper.admin.AdminMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AdminServiceImpl implements AdminService {

    private static final String MYBATIS_CONFIG_PATH = "/Users/choegyeongmin/Documents/인프런/자료/FC-Together/src/main/resources/mybatis-config.xml";
    private static final String ADMIN_MAPPER_PATH = "/Users/choegyeongmin/Documents/인프런/자료/FC-Together/src/main/resources/mapper/admin/adminMapper.xml";

    public static void main(String[] args) {
        // MyBatis 설정 파일 로드
        InputStream mybatisConfigInputStream = null;
        try {
            mybatisConfigInputStream = new FileInputStream(new File(MYBATIS_CONFIG_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // SqlSessionFactory 생성
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfigInputStream);

        // SqlSession 생성
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 매퍼 파일 경로 지정
        String mapperPath = ADMIN_MAPPER_PATH;

        try {
            // 매퍼 파일 로드
            InputStream mapperInputStream = new FileInputStream(new File(mapperPath));

            // 매퍼 파일 등록
            sqlSession.getConfiguration().addMapper(AdminMapper.class);

            // 매퍼 파일 실행
            AdminMapper adminMapper = sqlSession.getMapper(AdminMapper.class);

            // TODO: 실행할 매퍼 메서드 호출 - 데이터 조회
            List<TableData> tableDataList = adminMapper.getTableData();

            // 조회된 테이블 데이터 출력
            for (TableData tableData : tableDataList) {
                System.out.println(tableData);
            }

            // 세션 커밋
            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 세션 닫기
            sqlSession.close();
        }
    }
}